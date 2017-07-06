package models

import play.api.db.Database
import com.google.inject.Inject
import anorm._
import anorm.Macro
import anorm.RowParser

case class Usuario(id : Int, email: String, senha: String)

class UsuarioDAO @Inject()(database : Database){
    val parser : RowParser[models.Usuario] = Macro.namedParser[models.Usuario]
    
    def pesquisaPorEmail(email : String) = database.withConnection { implicit connection => 
         SQL("""SELECT * from tb_usuario where EMAIL = {email}""").on("email" -> email).as(parser.*)
    }
    
    /**
     * Insere novo usuario
     */
    def insert(user: Usuario) = database.withConnection { implicit connection => 
      val id: Option[Long] = SQL(
          """INSERT INTO TB_USUARIO (EMAIL, SENHA) 
              values ({email}, {senha})""")
       .on('email -> user.email,  
           'senha -> user.senha).executeInsert()
    }
}
package models

import play.api.db.Database
import com.google.inject.Inject
import anorm._
import anorm.Macro
import anorm.RowParser

case class Usuario(email: String, senha: String)

class UsuarioDAO @Inject()(database : Database){
    val parser : RowParser[models.Usuario] = Macro.namedParser[models.Usuario]
    
    def pesquisaPorEmail(email : String) = database.withConnection { implicit connection => 
         SQL("""SELECT * from tb_usuario where EMAIL = {email}""").on("email" -> email).as(parser.*)
    }
}
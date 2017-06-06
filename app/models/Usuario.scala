package models

import play.api.db.Database
import com.google.inject.Inject
import anorm.Macro
import anorm.RowParser

case class Usuario(email: String, senha: String)

/*object Usuario {
  var products = Set(
        Usuario("foo", "foo"), 
        Usuario("blah", "blah")
      )
      
   def pesquisaPorEmail(email : String) = products.filter(u => u.email == email).toList(0)  
}*/

class UsuarioDAO @Inject()(database : Database){
    //val parser : RowParser[models.Usuario] = Macro.namedParser[models.Usuario]
    
    def pesquisaPorEmail(email : String) = database.withConnection { implicit connection => 
        //val usuario : Usuario = SQL("""SELECT EMAIL, SENHA FROM TB_FILME WHERE EMAIL = {email}""").on('email -> email)
    }
}
package controllers

import play.api.mvc._
import models.Usuario
import models.UsuarioDAO
import com.google.inject.Inject
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.MessagesApi
import play.api.i18n.I18nSupport
import play.api.data.Form

@Singleton
class UsuarioController @Inject()(dao: UsuarioDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  
  def login = Action {
    Ok(views.html.users.login(userForm))
  }
  
  def loginSubmissao = Action { implicit request =>
    userForm.bindFromRequest.fold(
        formWithErrors => {
        BadRequest(views.html.users.login(formWithErrors))
      },
      usuario => {
        val usuarios = dao.pesquisaPorEmail(usuario.email)
        if(usuarios.size<1){ 
          Ok("Usuario não encontrado")
        }else{
          val user = usuarios.apply(0);
          if(usuario.senha == user.senha){
            Redirect("/filme")
          }else{
             Ok("Senha errada") 
          }
        }
      })
  }
  
  val userForm = Form(
    mapping(
        "Email" -> nonEmptyText,
        "Senha" -> nonEmptyText)
        (UserVO.apply)(UserVO.unapply)    
  ) 
}

case class UserVO(email: String, senha: String)
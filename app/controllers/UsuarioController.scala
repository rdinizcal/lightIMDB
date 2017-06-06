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
        BadRequest("Erro")
      },
      usuario => {
        Ok(dao.pesquisaPorEmail(usuario.email).toString())
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
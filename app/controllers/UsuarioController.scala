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
import play.api.mvc.Session

@Singleton
class UsuarioController @Inject()(usuarioDAO: UsuarioDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  
  def logout = Action {
    Redirect("/").withNewSession
  }
  
  def signup = Action {
    Ok(views.html.users.signup("")) 
  }
  
  def loginSubmissao = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.index("Preencher todos os campos."))
      },
      usuario => {
        val usuarios = usuarioDAO.pesquisaPorEmail(usuario.email)
        
        if(usuarios.size<1){ 
          BadRequest(views.html.index("Usuário e/ou senha incorretos."))
        }else{
          val user = usuarios.apply(0);
          if(usuario.senha == user.senha){
            Redirect("/filme").withSession("connected" -> user.id.toString())
          }else{
             BadRequest(views.html.index("Usuário e/ou senha incorretos.")) 
          }
        }
      })
  }
  
  def signupSubmissao = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.users.signup("Preencher todos os campos."))
      },
      usuario => {
        val usuarios = usuarioDAO.pesquisaPorEmail(usuario.email)
        
        if(usuarios.size>0){ 
          BadRequest(views.html.index("Usuário já existe no sistema."))
        }else{
          val novoUser = Usuario(0, usuario.email, usuario.senha)
          usuarioDAO.insert(novoUser)
          Ok(views.html.index(""))
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
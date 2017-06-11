package controllers

import play.api.mvc._
import models.Filme
import models.FilmeUsuario
import models.FilmeDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi


/**
 * A classe que processa as requisicoes do usuario 
 * relacionadas a filmes e interage com o meio de 
 * persitencia. 
 * 
 * Utiliza o mecanismo de injecao de dependencia do 
 * PlayFramework para ter acesso ao DAO FilmeDAO.  
 */
@Singleton
class FilmeController @Inject()(filmeDAO: FilmeDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  
  def listar = Action {request =>
    request.session.get("connected").map { user =>
      var filmes = filmeDAO.selectAll
      Ok(views.html.filmes.listagem(filmes, notaForm))
    }.getOrElse {
      Unauthorized(views.html.unauthorized())
    }
  }
  
  def novoFilme = Action { request =>
    request.session.get("connected").map { user =>
      Ok(views.html.filmes.novoFilme(filmeForm))
    }.getOrElse {
      Unauthorized(views.html.unauthorized())
    }
  }
  
  def novoFilmeSubmissao = Action { implicit request =>
    filmeForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("ERRO: novoFilme")
      },
      filme => {
        val novoFilme = Filme(0, filme.titulo, filme.diretor, filme.ano)
        filmeDAO.insert(novoFilme)
        var filmes = filmeDAO.selectAll
        Created(views.html.filmes.listagem(filmes, notaForm))
      }
    )
  }
  
  def avaliarFilme (filmeId : String) = Action { implicit request =>
    var filmes = filmeDAO.selectAll
    notaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("ERRO: avaliarFilme")
      },
      notaVO => {        
        request.session.get("connected").map { userId =>
          var filmeUsuario = FilmeUsuario(userId.toInt, notaVO.filmeId, notaVO.nota)
          var listFU = filmeDAO.selectByRating(filmeUsuario)
          
          if(listFU.size<1){
            filmeDAO.insertRating(filmeUsuario)
          }else{
            filmeDAO.updateRating(filmeUsuario)
          }
             
          Redirect("/filme")
          
        }.getOrElse {
          Unauthorized(views.html.unauthorized())
        }
      }
    )
    
    
  }
  
  val filmeForm = Form(
    mapping(
      "Titulo"  -> nonEmptyText,
      "Diretor" -> nonEmptyText,
      "Ano" -> number(min=1950, max=2050)
    )(FilmeVO.apply)(FilmeVO.unapply)    
  )
  
  val notaForm = Form(
    mapping(
        "FilmeId" -> number,
        "Nota" -> number(min=0, max=5)
    )(NotaVO.apply)(NotaVO.unapply)
    )
}

case class FilmeVO(titulo: String, diretor: String, ano: Int)

case class NotaVO(filmeId : Int, nota: Int)
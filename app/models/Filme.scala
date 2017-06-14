package models

import anorm.Macro
import anorm.RowParser
import anorm.SQL
import anorm.SqlStringInterpolation
import javax.inject.Inject
import play.api.db.Database

/**
 * A definicao da classe Filme, que corresponde 
 * a uma entidade cujo estado deve ser salvo no 
 * banco de dados. 
 */
case class Filme(id: Int, titulo: String, diretor: String, anoProducao: Int, media : Option[Double]) 

/**
 * A definicao da classe FilmeUsuario, que corresponde 
 * a um relacionamento n x m com avaliacao do
 * filme no banco de dados
 */
case class FilmeUsuario(idUsuario : Int, idFilme: Int, nota : Int) 

/**
 * Um DAO para a classe de entidade Filme. 
 */
class FilmeDAO @Inject() (database: Database){
  val filmeParser : RowParser[models.Filme] = Macro.namedParser[models.Filme]
  val filmeUsuarioParser : RowParser[models.FilmeUsuario] = Macro.namedParser[models.FilmeUsuario]
  
  /**
   * Insere novo filme
   */
  def insert(filme: Filme) = database.withConnection { implicit connection => 
    val id: Option[Long] = SQL(
        """INSERT INTO TB_FILME(TITULO, DIRETOR, ANO_PRODUCAO) 
            values ({titulo}, {diretor}, {anoProducao})""")
     .on('titulo -> filme.titulo, 
         'diretor -> filme.diretor, 
         'anoProducao -> filme.anoProducao).executeInsert()
  }
  
  /**
   * Seleciona todos os filmes
   */
  def selectAll : List[models.Filme] = database.withConnection { implicit connection => 
    val result : List [models.Filme] =
      SQL("""SELECT ID, TITULO, DIRETOR, ANO_PRODUCAO as ANOPRODUCAO, AVG(NOTA) as media
	        FROM TB_FILME fil
          LEFT JOIN TB_FILME_USUARIO fil_u
          ON fil.id = fil_u.id_filme
          GROUP BY ID
          ORDER BY TITULO""")
          .as(filmeParser.*)
          
    return result
  }
  
  /**
   * Seleciona nota que usuario deu para filme
   */
  def selectByRating(filmeUsuario : FilmeUsuario) = database.withConnection { implicit connection =>
    SQL("""SELECT * 
            FROM TB_FILME_USUARIO 
            WHERE ID_USUARIO = {idUsuario} AND ID_FILME = {idFilme}""")
        .on('idUsuario -> filmeUsuario.idUsuario,
            'idFilme -> filmeUsuario.idFilme)
        .as(filmeUsuarioParser.*)
  }
  
  /**
   * Atualiza nota que usuario deu para filme
   */
  def updateRating(filmeUsuario : FilmeUsuario) = database.withConnection { implicit connection =>
    SQL("""UPDATE TB_FILME_USUARIO 
            SET NOTA = {nota}
            WHERE ID_USUARIO = {idUsuario} AND ID_FILME = {idFilme}""")
        .on('idUsuario -> filmeUsuario.idUsuario,
            'idFilme -> filmeUsuario.idFilme,
            'nota -> filmeUsuario.nota)
        .executeUpdate()
  }
  
  /**
   * Insere nota que usuario deu para filme
   */
  def insertRating(filmeUsuario : FilmeUsuario) = database.withConnection { implicit connection =>
    SQL("""INSERT INTO TB_FILME_USUARIO(ID_USUARIO, ID_FILME, NOTA) 
            values ({idUsuario}, {idFilme}, {nota})""")
    .on('idUsuario -> filmeUsuario.idUsuario, 
         'idFilme -> filmeUsuario.idFilme, 
         'nota -> filmeUsuario.nota).executeInsert()
  }
  
  
  
}
package annuncio.dao;

import candidatura.service.CandidaturaService;
import candidatura.service.CandidaturaServiceInterface;
import storage.entity.Annuncio;
import storage.entity.Azienda;
import storage.entity.Candidatura;
import utils.ConPool;
import utils.KeywordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnuncioDAO {
    public static List<Annuncio> getAnnuncioById(int id) throws SQLException {
        List<Annuncio> result = new ArrayList<>();

        Connection connection = ConPool.getConnection();
        Statement stmt = (Statement) connection.createStatement();
        PreparedStatement pdstmt = connection.prepareStatement("SELECT * FROM Annuncio a WHERE a.ID = ?1");
        pdstmt.setInt(1, id);

        AziendaService aziendaService = new AziendaService();
        SedeService sedeService = new SedeService();
        CandidaturaService candidaturaService = new CandidaturaService();

        ResultSet rs = pdstmt.executeQuery();
        while (rs.next()) {
            result.add(
                    new Annuncio(id,
                            aziendaService.getAziendaByUtente(rs.getInt(2)),
                            rs.getBoolean(3),
                            SedeService.getSedeById(rs.getInt(4)),
                            rs.getInt(5),
                            rs.getString(6),
                            rs.getDate(7),
                            rs.getString(8),
                            KeywordUtils.getKeywordListFromString(rs.getString(9)),
                            PreferenzeUtils.getPreferenzeListFromString(rs.getString(10)),
                            rs.getString(11),
                            CandidaturaService.getCandidatureByAnnuncio(id)
                            )
            );
        }
        return result;
    }

    public static void creaAnnuncio(Annuncio annuncio) throws SQLException {

        Connection connection = ConPool.getConnection();
        Statement stmt = (Statement) connection.createStatement();
        PreparedStatement pdstmt = connection.prepareStatement(
                "INSERT INTO Annuncio(ID, Azienda, Attivo, Sede, N_Persone, Descrizione, Scadenza, Requisiti, Keyword, Preferenze, Ruolo)"+
                        "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11)");
        pdstmt.setInt(1, annuncio.getId());
        pdstmt.setInt(2, annuncio.getAzienda().getId());
        pdstmt.setBoolean(3, annuncio.isAttivo());
        pdstmt.setInt(4, annuncio.getSede().getId());
        pdstmt.setInt(5, annuncio.getNumeroPersone());
        pdstmt.setString(6, annuncio.getDescrizione());
        pdstmt.setDate(7, java.sql.Date.valueOf(annuncio.getDataScadenza().toLocalDate().toString()));
        pdstmt.setString(8, RequiritiUtils.getStringFromRequisitiList(annuncio.getRequisiti()));
        pdstmt.setString(9, KeywordUtils.getKeywordStringFromList(annuncio.getKeyword()));
        pdstmt.setString(10, PreferenzeUtils.getPreferenzeStringFromList(annuncio.getPreferenze()));
        pdstmt.setString(11, annuncio.getRuolo());

        pdstmt.executeQuery();
    }

    public static void modificaAnnuncio(Annuncio annuncio) throws SQLException{
        Connection connection = ConPool.getConnection();
        Statement stmt = (Statement) connection.createStatement();
        PreparedStatement pdstmt = connection.prepareStatement(
                "UPDATE Annuncio a SET a.Azienda = ?1, a.Attivo = ?2, a.Sede = ?3, a.N_Persone = ?4, " +
                        "a.Descrizione = ?5, a.Scadenza = ?6, a.Requisiti = ?7, a.Keyword = ?8, a.Preferenze = ?9, " +
                        "a.Ruolo = ?10 " +
                        "WHERE a.ID = ?11");
        pdstmt.setInt(1, annuncio.getAzienda().getId());
        pdstmt.setBoolean(2, annuncio.isAttivo());
        pdstmt.setInt(3, annuncio.getSede().getId());
        pdstmt.setInt(4, annuncio.getNumeroPersone());
        pdstmt.setString(5, annuncio.getDescrizione());
        pdstmt.setDate(6, java.sql.Date.valueOf(annuncio.getDataScadenza().toLocalDate().toString()));
        pdstmt.setString(7, RequiritiUtils.getStringFromRequisitiList(annuncio.getRequisiti()));
        pdstmt.setString(8, KeywordUtils.getKeywordStringFromList(annuncio.getKeyword()));
        pdstmt.setString(9, PreferenzeUtils.getPreferenzeStringFromList(annuncio.getPreferenze()));
        pdstmt.setString(10, annuncio.getRuolo());
        pdstmt.setInt(11, annuncio.getId());

        pdstmt.executeUpdate();
    }

    public static void eliminaAnnuncio(Annuncio annuncio) throws SQLException {
        Connection connection = ConPool.getConnection();
        Statement stmt = (Statement) connection.createStatement();
        PreparedStatement pdstmt = connection.prepareStatement(
                "DELETE FROM Annuncio a WHERE a.ID = $1");
        pdstmt.setInt(1, annuncio.getId());

        pdstmt.execute();
    }

    public static void chiusuraAnnuncio(Annuncio annuncio) throws SQLException {
        Connection connection = ConPool.getConnection();
        Statement stmt = (Statement) connection.createStatement();
        PreparedStatement pdstmt = connection.prepareStatement(
                "UPDATE Annuncio SET Attivo = ?1 WHERE ID = ?2");
        pdstmt.setBoolean(1, false);
        pdstmt.setInt(2, annuncio.getId());
        pdstmt.execute();
    }
}
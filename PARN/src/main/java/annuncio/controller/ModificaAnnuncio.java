package annuncio.controller;

import annuncio.service.AnnuncioService;
import annuncio.service.AnnuncioServiceInterface;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import storage.entity.Annuncio;
import storage.entity.Azienda;
import storage.entity.Utente;
import utente.service.UtenteService;
import utente.service.UtenteServiceInterface;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ModificaAnnuncio", value = "/ModificaAnnuncio")
public class ModificaAnnuncio extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        AnnuncioServiceInterface serviceAnnuncio = new AnnuncioService();
        UtenteServiceInterface serviceUtente = new UtenteService();

        if(utente != null && utente instanceof Azienda){
            Azienda azienda = (Azienda) utente;
            int idAnnuncio = -1;
            try{
                idAnnuncio = Integer.parseInt(request.getParameter("id_Annuncio"));
            }catch (NumberFormatException n){
                System.out.println("Conversion error " + n);
            }

            Annuncio annuncio = serviceAnnuncio.getAnnuncioById(idAnnuncio);

            try{
                annuncio.setSede(serviceUtente.getSedeById(azienda, Integer.parseInt(request.getParameter("id_Sede"))));
                annuncio.setNumeroPersone(Integer.parseInt(request.getParameter("numero_Persone")));
            }catch (NumberFormatException n){
                System.out.println("Conversion error " + n);
                throw new NullPointerException();
            }

            annuncio.setAttivo(Boolean.getBoolean(request.getParameter("attivo")));
            annuncio.setDescrizione(request.getParameter("descrizione"));
            annuncio.setDataScadenza(LocalDateTime.parse(request.getParameter("scadenza"), formatter));

            List<String> requisiti = new ArrayList<>();
            for(String s:request.getParameter("requisiti").split(","))
                requisiti.add(s);
            annuncio.setRequisiti(requisiti);

            List<String> keywords = new ArrayList<>();
            for(String s:request.getParameter("keywords").split(","))
                keywords.add(s);
            annuncio.setKeyword(keywords);

            List<String> preferenze = new ArrayList<>();
            for(String s:request.getParameter("preferenze").split(","))
                preferenze.add(s);
            annuncio.setPreferenze(preferenze);
            annuncio.setRuolo(request.getParameter("ruolo"));

            serviceAnnuncio.modificaAnnuncio(annuncio);
            serviceUtente.aggiornaAzienda(azienda);
            session.setAttribute("utente", azienda);
            request.getRequestDispatcher("./WEB_INF/visualizzaAnnunci.jsp").forward(request, response);
        }else response.sendRedirect(".");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

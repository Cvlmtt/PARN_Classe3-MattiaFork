package utente.cotroller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import storage.entity.Azienda;
import storage.entity.Persona;
import storage.entity.Utente;
import utente.service.UtenteService;
import utils.PasswordEncrypter;

import java.io.IOException;

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = PasswordEncrypter.encryptThisString(request.getParameter("password"));

        UtenteService service = new UtenteService();
        Utente utente = service.autenticazione(email, password);
        if(utente!=null && utente instanceof Azienda){
            Azienda azienda = (Azienda) utente;
            HttpSession session = request.getSession();
            session.setAttribute("utente", azienda);
            request.getRequestDispatcher("../webapp/WEB-INF/areaAzienda.jsp").forward(request, response);
        } else if(utente!=null && utente instanceof Persona){
            Persona persona = (Persona) utente;
            HttpSession session = request.getSession();
            session.setAttribute("utente", persona);
            request.getRequestDispatcher("../webapp/WEB-INF/areaPersona.jsp").forward(request, response);
        }else
            response.sendRedirect(".");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}

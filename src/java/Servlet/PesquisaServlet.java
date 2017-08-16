/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servlet;

import Control.Busca;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/*import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aluno
 */

//@WebServlet(urlPatterns = {"/PesquisaServlet"})
public class PesquisaServlet{ //extends HttpServlet {
    
    private Busca busca;
    //private Connection conn;
    //private Statement stmt;
   /*     
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        busca = new Busca();
        Thread thread = new Thread(busca);
        thread.start();
    
        
        /*try {            
            String driver = "org.hsqldb.jdbcDriver";                                
            String url = "jdbc:hsqldb:hsql"+"://10.65.215.32/xdb";
            String user = "sa";
            String passwd = "";
            
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, passwd);
            stmt = conn.createStatement();
        } catch (Exception e) {
            throw new ServletException(e);
        }*/
    //}

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /*
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        
        busca.getLinksToSearch(new ArrayList<String>()); 
        /*try (PrintWriter out = response.getWriter()) {
            try {
                ResultSet rs = stmt.executeQuery("select * from Users where LOGIN ='"+request.getParameter("login")+"'");
                
                while(rs.next()){
                    if (rs.getString("PASSWORD").equals(request.getParameter("password"))) {//cadastro com sucesso
                        request.setAttribute("mensagem", "Login realizado com sucesso!");
                        PersonBean person = new PersonBean(rs.getString("NAME"), rs.getString("LOGIN"), rs.getString("EMAIL")); 
                        request.setAttribute("pessoa", person); 
                        request.getRequestDispatcher("/Mensagem.jsp").forward(request, response);                
                    }else{//login inexistente
                        request.setAttribute("mensagem", "Senha incorreta!");
                        request.getRequestDispatcher("/Login.jsp").forward(request, response);
                    }
                    
                    return;
                }
                request.setAttribute("mensagem", "Usuário não Cadastrado!");
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
                
            } catch (SQLException ex) {
                Logger.getLogger(PesquisaServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ServletException ex) {
                Logger.getLogger(PesquisaServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }*/
   // }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   /* @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /*@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
   /* @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /*@Override
    public void destroy() {
        try {
            conn.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }                
    }*/
}

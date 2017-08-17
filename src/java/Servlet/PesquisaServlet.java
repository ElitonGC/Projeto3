package Servlet;

import Control.ResultadoBusca;
import Model.Documento;
import Model.ItemListaInvertida;
import Model.SearchBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aluno
 */
@WebServlet(urlPatterns = {"/PesquisaServlet"})
public class PesquisaServlet extends HttpServlet {

    private Map<String, List<ItemListaInvertida>> listaInvertida;
    private List<Documento> docs;
    private List<Documento> documentos;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("C:\\Testes\\listaInvertida.dat")));
            listaInvertida = (Map<String, List<ItemListaInvertida>>) in.readObject();
            in.close();

            in = new ObjectInputStream(new FileInputStream(new File("C:\\Testes\\docsBusca.dat")));
            docs = (List<Documento>) in.readObject();
            in.close();

            in = new ObjectInputStream(new FileInputStream(new File("C:\\Testes\\documentosBusca.dat")));
            documentos = (List<Documento>) in.readObject();
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PesquisaServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PesquisaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String[] parts = request.getParameter("pesquisa").split(" ");
            List<ResultadoBusca> resultBusca = new ArrayList<ResultadoBusca>();
            for (Documento doc : documentos) {
                ResultadoBusca rs = new ResultadoBusca();
                rs.setDocumento(doc);
                rs.setScore(doc.calcularScore(Arrays.asList(parts), documentos.size(), documentos.size()));
                resultBusca.add(rs);
            }
            Collections.sort(resultBusca);
            List<SearchBean> beans = new ArrayList<SearchBean>();
            for (ResultadoBusca rs : resultBusca) {
                SearchBean search = new SearchBean(rs.getDocumento().getLink(), rs.getDocumento().getTitle(), rs.getDocumento().getTitle());
                beans.add(search);
            }
            request.setAttribute("respostas", beans);
            request.getRequestDispatcher("/resultado.jsp").forward(request, response);
            response.sendRedirect("/resultado.jsp");
            System.out.println("1");
            /*while(rs.next()){
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
            request.getRequestDispatcher("/Login.jsp").forward(request, response);*/
        } catch (ServletException | IOException ex) {
            Logger.getLogger(PesquisaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    /*@Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>*/
    @Override
    public void destroy() {
        super.destroy();
    }

    public List<String> getLinksToSearch(String[] palavras) {
        List list = new ArrayList();
        for (String palavra : palavras) {
            if (listaInvertida.containsKey(palavra)) {
                for (ItemListaInvertida item : listaInvertida.get(palavra)) {
                    if (docs.contains(item.getCod())) {
                        list.add(docs.get(docs.indexOf(item.getCod())));
                    }
                }
            }
        }
        return list;
    }

}

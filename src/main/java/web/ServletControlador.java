/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package web;

import datos.ClienteDaoJDBC;
import dominio.Cliente;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author enriq
 */
@WebServlet(name = "ServletControlador", urlPatterns = {"/ServletControlador"})
public class ServletControlador extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("accion") != null) {
            switch (request.getParameter("accion")) {
                case "insertar":
                    this.insertarCliente(request, response);
                    break;
                case "editar":
                    this.editarCliente(request, response);
                    break;
                case "modificar":
                    this.modificarCliente(request, response);
                    break;
                case "eliminar":
                    this.eliminarCliente(request,response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        }else{
           this.accionDefault(request, response); 
        }
        
        
        
        try (PrintWriter out = response.getWriter()) {
        }
    }
    private void accionDefault(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        List<Cliente> clientes = new ClienteDaoJDBC().listar();
        System.out.println("clientes = " + clientes);
        HttpSession sesion = request.getSession();
        
        
        sesion.setAttribute("clientes", clientes);
        
        sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
        sesion.setAttribute("totalClientes", clientes.size());
        //request.getRequestDispatcher("clientes.jsp").forward(request, response);
        response.sendRedirect("clientes.jsp");
    }
    private double calcularSaldoTotal(List<Cliente> clientes){
        double saldoTotal = 0;
        for (Cliente cliente : clientes) {
            saldoTotal += cliente.getSaldo();
        }
        return saldoTotal;
    }
    
    private void insertarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        // Recuperar los valores del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !"".equals(saldoString)) {
            saldo = Double.parseDouble(saldoString);
        }
        
        // Creacion del objeto cliente
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);
        
        //Insertarmos en la base de datos
        int registrosModificados = new ClienteDaoJDBC().insertar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        
        this.accionDefault(request, response);
    }
    
    public void editarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        Cliente cliente = new ClienteDaoJDBC().encontrar(new Cliente(idCliente));
        request.setAttribute("cliente", cliente);
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        request.getRequestDispatcher(jspEditar).forward(request, response);
    }
    
    public void modificarCliente (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !"".equals(saldoString)) {
            saldo = Double.parseDouble(saldoString);
        }
        
        // Creacion del objeto cliente
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        
        //Insertarmos en la base de datos
        int registrosModificados = new ClienteDaoJDBC().actualizar(cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        
        this.accionDefault(request, response);
    }

    public void eliminarCliente (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        // Creacion del objeto cliente
        Cliente cliente = new Cliente(idCliente);
        
        //Eliminar en la base de datos
        int registrosModificados = new ClienteDaoJDBC().eliminar(   cliente);
        System.out.println("registrosModificados = " + registrosModificados);
        
        this.accionDefault(request, response);
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
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

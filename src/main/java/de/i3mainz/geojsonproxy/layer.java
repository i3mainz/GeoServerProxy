package de.i3mainz.geojsonproxy;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author florian.thiery
 */
public class layer extends HttpServlet {

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
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();

        try {

            String bereich = null;
            String layer = null;
            String bbox = null;
            String epsg = null;
            String filter = null;

            bereich = request.getParameter("bereich");
            layer = request.getParameter("layer");
            bbox = request.getParameter("bbox");
            epsg = request.getParameter("epsg");
            filter = request.getParameter("CQL_FILTER");  // returns null if parameter doesnt exist
            
            String geojson = "";
            
            // bbox query
            if (bereich != null && layer != null && bbox != null && epsg != null && filter == null) {
                geojson = GeoserverConnection.getAll(bereich, layer, bbox, epsg);
            // filter query  
            } else if (bereich != null && layer != null && epsg != null && filter != null && bbox == null) {
                geojson = GeoserverConnection.getAllFiltered(bereich, layer, epsg, filter);
            } else {
                out.print("invalid or missing parameters!");
            }

            out.print(geojson);

        } catch (Exception e) {
            String error = e.getMessage();
            out.print(error);
        } finally {
            out.close();
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
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

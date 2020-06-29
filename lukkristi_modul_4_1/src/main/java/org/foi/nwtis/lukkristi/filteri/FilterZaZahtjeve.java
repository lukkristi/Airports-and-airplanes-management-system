package org.foi.nwtis.lukkristi.filteri;

import java.io.IOException;
import java.sql.Timestamp;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.lukkristi.ejb.eb.Dnevnik;
import org.foi.nwtis.lukkristi.ejb.sb.DnevnikFacade;
import org.foi.nwtis.lukkristi.ejb.sb.DnevnikFacadeLocal;

/**
 *
 * @author mato
 */
@WebFilter(urlPatterns = {"/pogled_3.xhtml","/pogled_4.xhtml","/pogled_5.xhtml"})
public class FilterZaZahtjeve implements Filter {

    @EJB
    private DnevnikFacadeLocal dnevnikFacade;

    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        long pocetak = System.currentTimeMillis();
        String korisnik = "nepoznat";
        HttpServletResponse response = (HttpServletResponse) sr1;        
        HttpServletRequest request = (HttpServletRequest) sr;
        String contekstPutanja = (request.getRequestURI() != null && request.getRequestURI() != "") ? request.getContextPath() : "";
        String servletPutanja = (request.getServletPath() != null && request.getServletPath() != "") ? request.getServletPath() : "";
        String putanjaInfo = (request.getPathInfo() != null && request.getPathInfo() != "") ? request.getPathInfo() : "";
        String url = contekstPutanja + servletPutanja + putanjaInfo;
        String ipadresa = (request.getRemoteAddr() != null && request.getRemoteAddr() != "") ? request.getRemoteAddr() : "";
        HttpSession sesija = ((HttpServletRequest) request).getSession(false);
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/prijava.xhtml";
        if (sesija != null) {
            if (sesija.getAttribute("korisnik") != null) {
                korisnik = (String) sesija.getAttribute("korisnik");
            }
            else{
                response.sendRedirect(loginURI);}
        }
        else{
            response.sendRedirect(loginURI);
        }            
        fc.doFilter(sr, sr1);
        long kraj = System.currentTimeMillis();
        if (url != "" && !korisnik.equals("nepoznat")) {
            Dnevnik dn = new Dnevnik();
            dn.setIpadresa(ipadresa);
            dn.setKorisnik(korisnik);
            dn.setTrajanje((int)(kraj - pocetak));
            dn.setUrl(url);
            dn.setVriejme(new Timestamp(System.currentTimeMillis()));
            dnevnikFacade.create(dn);
        }
    }

    @Override
    public void destroy() {
    }
}

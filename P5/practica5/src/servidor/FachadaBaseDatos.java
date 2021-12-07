/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.sql.Connection;
import java.sql.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Esther
 */
public class FachadaBaseDatos {

    private Connection conexion;

    public FachadaBaseDatos() {
    }

    public void conexionBD() {

        Properties configuracion = new Properties();
        FileInputStream arqConfiguracion;

        try {
            arqConfiguracion = new FileInputStream("baseDatos.properties");
            configuracion.load(arqConfiguracion);
            arqConfiguracion.close();

            Properties usuario = new Properties();

            String gestor = configuracion.getProperty("gestor");

            usuario.setProperty("user", configuracion.getProperty("usuario"));
            usuario.setProperty("password", configuracion.getProperty("clave"));
            this.conexion = (Connection) java.sql.DriverManager.getConnection("jdbc:" + gestor + "://"
                    + configuracion.getProperty("servidor") + ":"
                    + configuracion.getProperty("puerto") + "/"
                    + configuracion.getProperty("baseDatos"),
                    usuario);

        } catch (FileNotFoundException i) {
            System.out.println(i.getMessage());
        } catch (IOException | java.sql.SQLException i) {
            System.out.println(i.getMessage());
        }

    }

    public boolean comprobarCredenciais(String usuario, String contrasinal) {
        PreparedStatement stmUsuario;
        ResultSet rsUsuario;

        try {
            stmUsuario = conexion.prepareStatement("SELECT id \n"
                    + "FROM usuarios \n"
                    + "WHERE id = ? AND contrasinal = crypt(?, contrasinal)");

            stmUsuario.setString(1, usuario);
            stmUsuario.setString(2, contrasinal);
            rsUsuario = stmUsuario.executeQuery();

            if (rsUsuario.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FachadaBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public boolean engadirUsuario(String us, String contrasenha) {
        PreparedStatement stmUsuario;
        try {
            stmUsuario = conexion.prepareStatement("INSERT into usuarios\n"
                    + "VALUES(?,crypt(?, gen_salt('md5')))");
            stmUsuario.setString(1, us);
            stmUsuario.setString(2, contrasenha);
            stmUsuario.executeUpdate();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean engadirSolicitude(String solicitante, String receptor) {

        PreparedStatement stmUsuario;
        try {
            stmUsuario = conexion.prepareStatement("INSERT into pediramizade\n"
                    + "VALUES(?,?)");
            stmUsuario.setString(1, solicitante);
            stmUsuario.setString(2, receptor);
            stmUsuario.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error engadirSolicitude. " + e.getMessage());
            return false;
        }

        return true;
    }

    private boolean engadirAmizade(String usuario1, String usuario2) {
        PreparedStatement stmUsuario;
        try {
            stmUsuario = conexion.prepareStatement("INSERT into seramigo\n"
                    + "VALUES(?,?)");
            stmUsuario.setString(1, usuario1);
            stmUsuario.setString(2, usuario2);
            stmUsuario.executeUpdate();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean eliminarSolicitude(String solicitante, String receptor) {
        PreparedStatement stmUsuario;
        try {
            stmUsuario = conexion.prepareStatement("delete \n"
                    + "from pediramizade \n"
                    + "where solicitante = ? and receptor = ?");
            stmUsuario.setString(1, solicitante);
            stmUsuario.setString(2, receptor);
            stmUsuario.executeUpdate();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean aceptarSolicitude(String solicitante, String receptor) {
        if (!eliminarSolicitude(solicitante, receptor)) {
            return false;
        }

        return engadirAmizade(solicitante, receptor);

    }

    public List<String> buscarUsuariosSolicitarAmizade(String patron, String nomeUser) {
        List<String> usuarios = new ArrayList<>();
        PreparedStatement stmUsuario;
        ResultSet rsUsuario;

        try {
            stmUsuario = conexion.prepareStatement("select id \n"
                    + "from usuarios \n"
                    + "where id like ?\n"
                    + "except\n"
                    + "select usuario1\n"
                    + "from seramigo\n"
                    + "where usuario2=?\n"
                    + "except\n"
                    + "select usuario2\n"
                    + "from seramigo\n"
                    + "where usuario1=?\n"
                    + "except\n"
                    + "select solicitante\n"
                    + "from pediramizade\n"
                    + "where receptor = ?\n"
                    + "except\n"
                    + "select receptor\n"
                    + "from pediramizade\n"
                    + "where solicitante = ?");

            stmUsuario.setString(1, "%" + patron + "%");
            stmUsuario.setString(2, nomeUser);
            stmUsuario.setString(3, nomeUser);
            stmUsuario.setString(4, nomeUser);
            stmUsuario.setString(5, nomeUser);
            rsUsuario = stmUsuario.executeQuery();

            while (rsUsuario.next()) {
                String usuario = rsUsuario.getString("id");
                if (!usuario.equals(nomeUser)) {
                    usuarios.add(usuario);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(FachadaBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuarios;
    }

    public List<String> buscarSolicitantesAmizade(String usuario) {
        List<String> usuarios = new ArrayList<>();
        PreparedStatement stmUsuario;
        ResultSet rsUsuario;

        try {
            stmUsuario = conexion.prepareStatement("select solicitante \n"
                    + "from pedirAmizade \n"
                    + "where receptor = ?");

            stmUsuario.setString(1, usuario);
            rsUsuario = stmUsuario.executeQuery();

            while (rsUsuario.next()) {
                usuarios.add(rsUsuario.getString("solicitante"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FachadaBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuarios;
    }

    public List<String> buscarAmigos(String usuario) {

        List<String> amigos = new ArrayList<String>();
        PreparedStatement stmUsuarios1, stmUsuarios2;
        ResultSet rsUsuario1, rsUsuario2;

        try {
            stmUsuarios1 = conexion.prepareStatement("select usuario2 \n"
                    + "from seramigo \n"
                    + "where usuario1 = ?");

            stmUsuarios1.setString(1, usuario);
            rsUsuario1 = stmUsuarios1.executeQuery();
            while (rsUsuario1.next()) {
                amigos.add(rsUsuario1.getString("usuario2"));
            }
            stmUsuarios2 = conexion.prepareStatement("select usuario1 \n"
                    + "from seramigo \n"
                    + "where usuario2 = ?");
            stmUsuarios2.setString(1, usuario);
            rsUsuario2 = stmUsuarios2.executeQuery();
            while (rsUsuario2.next()) {
                amigos.add(rsUsuario2.getString("usuario1"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return amigos;

    }

}

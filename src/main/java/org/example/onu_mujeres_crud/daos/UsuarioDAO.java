package org.example.onu_mujeres_crud.daos;

import org.example.onu_mujeres_crud.beans.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    private String url;
    private String user;
    private String pass;

    public UsuarioDAO(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public Usuario iniciarSesion(String correo, String contrasena) {
        Usuario usuario = null;
        String sql = "SELECT usuarioId, rolId, nombre, apellidoPaterno, apellidoMaterno, dni, correo, contrasenaHash FROM usuarios WHERE correo = ? AND contrasenaHash = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            pstmt.setString(2, contrasena); // En una aplicación real, esto debería ser la contraseña hasheada

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setUsuarioId(rs.getInt("usuarioId"));
                    usuario.setRolId(rs.getInt("rolId"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellidoPaterno(rs.getString("apellidoPaterno"));
                    usuario.setApellidoMaterno(rs.getString("apellidoMaterno"));
                    usuario.setDni(rs.getString("dni"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContrasenaHash(rs.getString("contrasenaHash"));
                    // No se cargan todos los campos para el inicio de sesión, solo los necesarios
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public boolean registrarUsuario(String nombre, String correo, String contrasena) {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasenaHash) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, correo);
            pstmt.setString(3, contrasena); // En una aplicación real, esto debería ser la contraseña hasheada

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
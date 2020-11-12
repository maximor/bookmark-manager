package com.personal.bookmarkmanager.DAO;

import java.util.Date;

public class ErrorMessageDAO {
    private int estado;
    private Date fecha;
    private String error;
    private String mensaje;
    private String ruta;

    public ErrorMessageDAO() {
    }

    public ErrorMessageDAO(int estado, Date fecha, String error, String mensaje, String ruta) {
        this.estado = estado;
        this.fecha = fecha;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}

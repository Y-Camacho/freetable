package com.camacho.proyecto.freetable.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENTE") // Valor que se guardará en la columna "tipo_usuario"
public class Cliente extends Usuario {

    private String name;
    private String phone;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String nombre) { this.name = nombre; }

    public String getPhone() { return phone; }
    public void setPhone(String telefono) { this.phone = telefono; }
    
    @Override
    public String toString() {
        return "Cliente [nombre=" + name + ", telefono=" + phone + "]";
    }
}
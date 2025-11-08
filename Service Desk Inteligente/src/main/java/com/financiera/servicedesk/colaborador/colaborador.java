package com.financiera.servicedesk.colaborador;
import com.financiera.servicedesk.rol.rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class colaborador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(length = 50)
    private String area;

    @Column(length = 50)
    private String cargo;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(length = 9)
    private String telefono;

    @Column(length = 40)
    private String contraseña;

    // FK relación con ROL
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private rol rol;
}



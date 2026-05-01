package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;

import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.OpcionRespuesta;
import com.arquitectura.motor_decisiones.entity.Progreso;
import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import com.arquitectura.motor_decisiones.repository.OpcionRespuestaRepository;
import com.arquitectura.motor_decisiones.repository.ProgresoRepository;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EvaluacionService {

    //1RO TRAERME EL REPOSITORY
    private final OpcionRespuestaRepository opcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final LeccionRepository leccionRepository;
    private final ProgresoRepository progresoRepository;

    public EvaluacionService(
            OpcionRespuestaRepository opcionRepository,
            UsuarioRepository usuarioRepository,
            LeccionRepository leccionRepository,
            ProgresoRepository progresoRepository
    ){
        this.opcionRepository  = opcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.leccionRepository = leccionRepository;
        this.progresoRepository = progresoRepository;
    }

    public FeedbackDTO evaluarDecision(RespuestaEstudianteDTO dto){
        // 2. Extraer todos los datos necesarios abriendo las "cajas fuertes" (Optionals)
        OpcionRespuesta opcionSeleccionada = opcionRepository.findById(dto.opcionSeleccionadaId())
                .orElseThrow(()->new RuntimeException("Error: opcion no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId()).
                orElseThrow(()->new RuntimeException("Error: Usuario no encontrado"));

        Leccion leccion = leccionRepository.findById(dto.leccionId()).
                orElseThrow(()-> new RuntimeException("Error: Leccion no encontrada"));

        // 3. Evaluar la regla de negocio
        boolean esCorrecto = opcionSeleccionada.getEsCorrecta();
        String mensaje = opcionSeleccionada.getJustificacionFeedback();

        // 4. Crear la huella histórica (Persistencia)

        Progreso nuevoProgreso=new Progreso();
        nuevoProgreso.setUsuario(usuario);
        nuevoProgreso.setLeccion(leccion);
        nuevoProgreso.setFechaIntento(LocalDateTime.now());
        nuevoProgreso.setCompletado(esCorrecto);
        nuevoProgreso.setPuntajeObtenido(esCorrecto?leccion.getPuntosrecompensa():0);
        nuevoProgreso.setNivelAlcanzado("Principiante");
        //5. se guarda el progreso
        progresoRepository.save(nuevoProgreso);

        //6. Se le empaqueta en un dto y se le envia al front
        return new FeedbackDTO(esCorrecto,mensaje);
    }
}

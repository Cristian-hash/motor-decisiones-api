package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.NivelMapaDTO;
import com.arquitectura.motor_decisiones.entity.Patron;
import com.arquitectura.motor_decisiones.repository.PatronRepository;
import com.arquitectura.motor_decisiones.repository.ProgresoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapaService {
    private final PatronRepository patronRepository;
    private final ProgresoRepository progresoRepository;

    public MapaService(PatronRepository patronRepository, ProgresoRepository progresoRepository) {
        this.patronRepository = patronRepository;
        this.progresoRepository = progresoRepository;
    }

    public List<NivelMapaDTO> obtenerMapaDelUsuario(Long usuarioId) {
        List<Patron> patrones = patronRepository.findAll();
        List<NivelMapaDTO> mapa = new ArrayList<>();

        boolean encontramosElActivo = false;

        for (Patron patron : patrones) {
            Long idPatron = patron.getId();

            // 1. Extraemos la ruta completa y la ordenamos matemáticamente
            List<Long> rutaOriginal = patron.getLecciones().stream()
                    .map(leccion -> leccion.getId())
                    .sorted()
                    .toList();

            // 2. EL FILTRO INTELIGENTE: Creamos una nueva mochila solo para lecciones pendientes
            List<Long> rutaPendiente = new ArrayList<>();
            for (Long idLeccion : rutaOriginal) {
                // Consultamos si esta lección específica ya fue ganada
                boolean superada = progresoRepository.existsByUsuarioIdAndLeccionIdAndCompletadoTrue(usuarioId, idLeccion);

                // Si NO está superada, la agregamos a los retos pendientes
                if (!superada) {
                    rutaPendiente.add(idLeccion);
                }
            }

            // 3. El viaje iniciará en el primer reto que falte
            Long leccionInicial = rutaPendiente.isEmpty() ? 0L : rutaPendiente.get(0);

            String estado;
            String icono;

            // 4. Nueva regla absoluta: Si ya no hay retos pendientes, el nivel entero está completado
            if (rutaPendiente.isEmpty() && !rutaOriginal.isEmpty()) {
                estado = "COMPLETADO";
                icono = "⭐";
            } else if (!encontramosElActivo && !rutaOriginal.isEmpty()) {
                estado = "ACTIVO";
                icono = "🚀";
                encontramosElActivo = true;
            } else {
                estado = "BLOQUEADO";
                icono = "🔒";
            }

            // 5. Enviamos a Angular ÚNICAMENTE la ruta de pendientes
            mapa.add(new NivelMapaDTO(idPatron, patron.getNombre(), leccionInicial, estado, icono, rutaPendiente));
        }
        return mapa;
    }
}
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
        Long ultimaLeccionCompletada = progresoRepository.findUltimaLeccionCompletada(usuarioId);
        List<Patron> patrones = patronRepository.findAll();
        List<NivelMapaDTO> mapa = new ArrayList<>();

        boolean encontramosElActivo = false;

        for (Patron patron : patrones) {
            Long idPatron = patron.getId();

            // Extraemos dinámicamente la primera lección que le pertenece a este patrón
            Long leccionInicial = patron.getLecciones().isEmpty() ? 1L : patron.getLecciones().get(0).getId();

            String estado;
            String icono;

            if (leccionInicial <= ultimaLeccionCompletada) {
                estado = "COMPLETADO";
                icono = "⭐";
            } else if (!encontramosElActivo) {
                estado = "ACTIVO";
                icono = "🚀";
                encontramosElActivo = true;
            } else {
                estado = "BLOQUEADO";
                icono = "🔒";
            }

            mapa.add(new NivelMapaDTO(idPatron, patron.getNombre(), leccionInicial, estado, icono));
        }
        return mapa;
    }
}
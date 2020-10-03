package org.sid.entities;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "p1",types = org.sid.entities.Projection.class)
public interface ProjectionPr {
public Long getId();
public double getPrix();
public Date getDateDeProjection();
public Salle getSalle();
public Film getFilm();
public Seance getSeance();
public Collection<Ticket> getTickets();
}

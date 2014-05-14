package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
public class Training extends Identifiable {
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@DescriptionsList
	private Human sportsman;
	
	@Required @Column(length=40)
	private String description;
	
	@ElementCollection
	private Collection<TrainingSession> sessions;
	
	public Human getSportsman() {
		return sportsman;
	}

	public void setSportsman(Human sportsman) {
		this.sportsman = sportsman;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<TrainingSession> getSessions() {
		return sessions;
	}

	public void setSessions(Collection<TrainingSession> sessions) {
		this.sessions = sessions;
	}

}

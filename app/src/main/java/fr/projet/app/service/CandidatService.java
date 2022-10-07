package fr.projet.app.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.projet.app.model.Candidat;
import fr.projet.app.model.Document;
import fr.projet.app.model.Education;
import fr.projet.app.model.Experience;
import fr.projet.app.repository.CandidatRepository;
import fr.projet.app.repository.CandidatRepositoryCustom;
import fr.projet.app.repository.DocumentRepository;
//import fr.projet.app.repository.EducationRepository;
import fr.projet.app.repository.ExperienceRepository;

@Service
public class CandidatService 
{
	@Autowired
	private CandidatRepository candidatRepository;

	//@Autowired
	//private EducationRepository educationRepository;
	
	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private ExperienceRepository experienceRepository;

	@Autowired
	private CandidatRepositoryCustom candidatRepositoryCustom;
	
	@Autowired
	private EducationService educationService;
	
	
//Candidat
	public List<Candidat> findAllCandidats() 
	{
		return candidatRepository.findAll();
	}

	public Candidat findCandidatById(int id) 
	{
		return candidatRepository.findById(id).orElseThrow();
	}

	public Candidat findCandidatByName(Candidat candidat) 
	{
		return candidatRepository.findByName(candidat.getPrenom(),candidat.getNom());
	}
	
	public Candidat createCandidat(Candidat candidat)
	{
		int id = candidatRepositoryCustom.createCandidat(candidat);
		Candidat candidatInserted = findCandidatById(id);
		return candidatInserted;
	}

	public void deleteCandidatById(int id) 
	{
		candidatRepository.deleteById(id);
	}

	
//Education
	@Transactional
	public Set<Education> findEducationsByCandidatId(int id) 
	{
		return candidatRepository.findEducationsByCandidatId(id);
	}

	
	@Transactional
	public Education addEducation(int idCandidat, Education edc) throws Exception
	{
		try
		{
			Education education       = educationService.createEducation(edc);
			Candidat candidat         = candidatRepository.findById(idCandidat).orElseThrow();
			Set<Education> educations = candidat.getEducations();
			educations.add(education);
			return education;
		}
		catch(Exception excep)
		{
			throw new Exception("Erreur CandidatService - createEducation(): " + excep);
		}
	}
	
		
	
//Document
	public Set<Document> findDocumentsByCandidatId(int id)
	{
		return candidatRepository.findDocumentsByCandidatId(id);
	}

	@Transactional
	public Document createDocument(int id, Document document)
	{
		Candidat candidat       = candidatRepository.findById(id).orElseThrow();
		Set<Document> documents = candidat.getDocuments();
		Document newDocument    = documentRepository.save(document);
		documents.add(newDocument);
		return documents.stream().filter(e -> e.equals(document)).findFirst().get();
	}


//Experience
	public Set<Experience> findExperiencesByCandidatId(int id) 
	{
		return candidatRepository.findExperiencesByCandidatId(id);
	}

	@Transactional
	public Experience createExperience(int id, Experience experience) 
	{
		Candidat candidat           = candidatRepository.findById(id).orElseThrow();
		Set<Experience> experiences = candidat.getExperiences();
		Experience newExperience    = experienceRepository.save(experience);
		experiences.add(newExperience);
		return experiences.stream().filter(e -> e.equals(experience)).findFirst().get();
	}
}
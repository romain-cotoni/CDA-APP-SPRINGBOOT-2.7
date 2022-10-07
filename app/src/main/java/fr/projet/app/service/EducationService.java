package fr.projet.app.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.projet.app.model.Diplome;
import fr.projet.app.model.Education;
import fr.projet.app.model.Specialite;
import fr.projet.app.repository.EducationRepository;

@Service
public class EducationService 
{
	@Autowired
	EducationRepository educationRepository;
	
	@Autowired
	DiplomeService diplomeService;
	
	@Autowired
	SpecialiteService specialiteService;
	
	
	public List<Education> findAllEducations()
	{
		return educationRepository.findAll();
	}
	
	
	public Education getEducation(int idEducation) 
	{
		return educationRepository.findById(idEducation).get();
	}

	
	@Transactional
	public Education createEducation(Education edc) throws Exception
	{
		try
		{
			Specialite specialite = specialiteService.createSpecialite(edc.getSpecialite());
			if(specialite != null) edc.setSpecialite(specialite);
			
			Diplome diplome = diplomeService.createDiplome(edc.getDiplome());
			if(diplome != null) edc.setDiplome(diplome);				
			
			Education education = educationRepository.save(edc);
			
			return education;
		}
		catch(Exception excep)
		{
			throw new Exception("Erreur EducationService - createEducation() : " + excep);
		}
	}

	
	@Transactional
	public Education updateEducation(int idEducation, Education edc) throws Exception
	{
		try
		{
			Education education = educationRepository.findById(idEducation).orElseThrow();
			education.setRecu(edc.getRecu());
			education.setLieu(edc.getLieu());
			education.setEcole(edc.getEcole());
			education.setDebut(edc.getDebut());
			education.setFin(edc.getFin());
			education.setInfo(edc.getInfo());
			
			Specialite specialite = specialiteService.updateSpecialite(edc.getSpecialite());
			education.setSpecialite(specialite);
			 
			 
			Diplome diplome = diplomeService.updateDiplome(edc.getDiplome());
			education.setDiplome(diplome);

			return educationRepository.save(education);
		}
		catch(Exception excep)
		{
			throw new Exception("Erreur EducationService - updateEducation(): " + excep);
		}
	}
	
	
	//@Transactional
	public void deleteEducation(int idEducation) 
	{
		Optional<Education> optionalEdc = educationRepository.findById(idEducation);
		if(optionalEdc.isPresent())
		{
			int idSpl = optionalEdc.get().getSpecialite().getIdSpecialite();
			int idDpl = optionalEdc.get().getDiplome().getIdDiplome();
				
			educationRepository.deleteById(idEducation);
			System.out.println("idEducation : "+idEducation);
			
			Optional<Specialite> optionalSpl = specialiteService.findSpecialiteById(idSpl);
			if(optionalSpl.isPresent())
			{
				System.out.println("spl Size : "+optionalSpl.get().getEducations().size());
				if(optionalSpl.get().getEducations().size() < 1)
				{
					System.out.println("deleting : "+idSpl);
					specialiteService.deleteSpecialite(idSpl);	
				}
			}
			
			Optional<Diplome> optionalDpl = diplomeService.findDiplomeById(idDpl);
			if(optionalDpl.isPresent())
			{
				System.out.println("dpl Size : "+optionalDpl.get().getEducations().size());
				if(optionalDpl.get().getEducations().size() < 1)
				{
					System.out.println("deleting : "+idDpl);
					diplomeService.deleteDiplome(idDpl);
				}
			}
			System.out.println("resultat : "+idEducation +" - "+idSpl+" - "+idDpl);
	
		}
	}
}







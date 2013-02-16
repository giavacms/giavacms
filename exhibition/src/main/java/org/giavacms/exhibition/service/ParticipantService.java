package org.giavacms.exhibition.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.common.model.Search;
import org.giavacms.exhibition.model.Artist;
import org.giavacms.exhibition.model.Institute;
import org.giavacms.exhibition.model.Museum;
import org.giavacms.exhibition.model.Participant;
import org.giavacms.exhibition.model.Sponsor;
import org.giavacms.exhibition.model.Subject;
import org.giavacms.exhibition.model.Testimonial;
import org.giavacms.exhibition.repository.ArtistRepository;
import org.giavacms.exhibition.repository.InstituteRepository;
import org.giavacms.exhibition.repository.MuseumRepository;
import org.giavacms.exhibition.repository.ParticipantRepository;
import org.giavacms.exhibition.repository.SponsorRepository;
import org.giavacms.exhibition.repository.TestimonialRepository;
import org.jboss.logging.Logger;

@Stateless
@LocalBean
public class ParticipantService
{

   @Inject
   ParticipantRepository participantRepository;

   @Inject
   MuseumRepository museumRepository;

   @Inject
   ArtistRepository artistRepository;

   @Inject
   InstituteRepository instituteRepository;

   @Inject
   TestimonialRepository testimonialRepository;

   @Inject
   SponsorRepository sponsorRepository;

   Logger logger = Logger.getLogger(getClass()
            .getCanonicalName());

   public List<? extends Subject> getList(Search<Participant> search, int startRow, int pageSize)
   {
      if (Museum.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            List<Museum> museumList = new ArrayList<Museum>();
            List<Participant> list = participantRepository.getList(search, startRow, pageSize);
            for (Participant participant : list)
            {
               Museum museum = museumRepository.find(participant.getSubject().getId());
               museumList.add(museum);
            }
            return museumList;
         }
         else
         {
            return museumRepository.getList(new Search<Museum>(Museum.class), startRow, pageSize);
         }
      }
      else if (Artist.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            List<Artist> artistList = new ArrayList<Artist>();
            List<Participant> list = participantRepository.getList(search, startRow, pageSize);
            for (Participant participant : list)
            {
               Artist artist = artistRepository.find(participant.getSubject().getId());
               artistList.add(artist);
            }
            return artistList;
         }
         else
         {
            return artistRepository.getList(new Search<Artist>(Artist.class), startRow, pageSize);
         }
      }
      else if (Institute.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            List<Institute> instituteList = new ArrayList<Institute>();
            List<Participant> list = participantRepository.getList(search, startRow, pageSize);
            for (Participant participant : list)
            {
               Institute institute = instituteRepository.find(participant.getSubject().getId());
               instituteList.add(institute);
            }
            return instituteList;
         }
         else
         {
            return instituteRepository.getList(new Search<Institute>(Institute.class), startRow, pageSize);
         }
      }
      else if (Testimonial.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            List<Testimonial> testimonialList = new ArrayList<Testimonial>();
            List<Participant> list = participantRepository.getList(search, startRow, pageSize);
            for (Participant participant : list)
            {
               Testimonial testimonial = testimonialRepository.find(participant.getSubject().getId());
               testimonialList.add(testimonial);
            }
            return testimonialList;
         }
         else
         {
            return testimonialRepository.getList(new Search<Testimonial>(Testimonial.class), startRow, pageSize);
         }

      }
      else if (Sponsor.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            List<Sponsor> sponsorList = new ArrayList<Sponsor>();
            List<Participant> list = participantRepository.getList(search, startRow, pageSize);
            for (Participant participant : list)
            {
               Sponsor sponsor = sponsorRepository.find(participant.getSubject().getId());
               sponsorList.add(sponsor);
            }
            return sponsorList;
         }
         else
         {
            return sponsorRepository.getList(new Search<Sponsor>(Sponsor.class), startRow, pageSize);
         }
      }

      return null;
   }

   public int getListSize(Search<Participant> search)
   {
      if (Museum.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            return participantRepository.getListSize(search);
         }
         else
         {
            return museumRepository.getListSize(new Search<Museum>(Museum.class));
         }
      }
      else if (Artist.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            return participantRepository.getListSize(search);
         }
         else
         {
            return artistRepository.getListSize(new Search<Artist>(Artist.class));
         }
      }
      else if (Institute.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            return participantRepository.getListSize(search);
         }
         else
         {
            return instituteRepository.getListSize(new Search<Institute>(Institute.class));
         }
      }
      else if (Testimonial.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            return participantRepository.getListSize(search);
         }
         else
         {
            return testimonialRepository.getListSize(new Search<Testimonial>(Testimonial.class));
         }
      }
      else if (Sponsor.TYPE.equals(search.getObj().getSubject().getType()))
      {
         if (search.getObj().getExhibition().getId() != null && !search.getObj().getExhibition().getId().isEmpty())
         {
            return participantRepository.getListSize(search);
         }
         else
         {
            return sponsorRepository.getListSize(new Search<Sponsor>(Sponsor.class));
         }
      }
      return 0;
   }

}

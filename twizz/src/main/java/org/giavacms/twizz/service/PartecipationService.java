package org.giavacms.twizz.service;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.giavacms.twizz.model.Argument;
import org.giavacms.twizz.model.Partecipation;
import org.giavacms.twizz.model.QuizCompetitor;
import org.giavacms.twizz.repository.ArgumentRepository;
import org.giavacms.twizz.repository.PartecipationRepository;
import org.giavacms.twizz.repository.QuizCompetitorRepository;
import org.giavacms.twizz.util.QuestionUtils;

@Stateless
@LocalBean
public class PartecipationService
{

   @Inject
   QuizCompetitorRepository quizCompetitorRepository;

   @Inject
   PartecipationRepository partecipationRepository;

   @Inject
   ArgumentRepository argumentRepository;

   @Inject
   MessageService messageService;

   public Partecipation loadPartecipation(String from)
   {
      QuizCompetitor quizCompetitor = quizCompetitorRepository.findByPhone(from, true);
      if (quizCompetitor != null)
      {
         quizCompetitor.setConfirmationDate(new Date());
         quizCompetitor.setPhone(from);
         quizCompetitorRepository.update(quizCompetitor);
         System.out.println("FOUND: " + quizCompetitor.getFullName());
         Argument argument = argumentRepository.fetch(quizCompetitor.getArgument().getId());
         Partecipation partecipation = QuestionUtils.generatePartecipation(argument, quizCompetitor);
         partecipationRepository.persist(partecipation);
         return partecipation;

      }
      return null;
   }

   public void end(Partecipation partecipation)
   {
      partecipation.setDateEnd(new Date());
      QuestionUtils.calculateScore(partecipation);
      quizCompetitorRepository.update(partecipation.getQuizCompetitor());
      partecipationRepository.update(partecipation);
      messageService.send(partecipation.getQuizCompetitor());
   }
}

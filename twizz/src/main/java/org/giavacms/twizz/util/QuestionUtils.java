package org.giavacms.twizz.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.giavacms.twizz.model.Argument;
import org.giavacms.twizz.model.Partecipation;
import org.giavacms.twizz.model.Question;
import org.giavacms.twizz.model.QuizCompetitor;
import org.giavacms.twizz.model.Reply;

public class QuestionUtils
{

   static Logger logger = Logger.getLogger(QuestionUtils.class.getName());

   public static Partecipation generatePartecipation(Argument argument, QuizCompetitor quizCompetitor)
   {
      logger.info("COMPETITOR: " + quizCompetitor);
      Partecipation partecipation = new Partecipation();
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MINUTE, quizCompetitor.getMinutes().intValue());
      partecipation.setDateStart(cal.getTime());
      partecipation.setQuizCompetitor(quizCompetitor);
      for (Question question : argument.getQuestions())
      {
         Reply reply = new Reply();
         reply.setQuestion(question);
         reply.setPoints(0);
         partecipation.addReply(reply);
      }
      for (int i = 0; i < partecipation.getReplies().size(); i++)
      {
         partecipation.add(i);
      }
      Collections.shuffle(partecipation.getIndices());
      return partecipation;
   }

   public static void calculateScore(Partecipation partecipation)
   {
      int total = 0;
      for (Reply reply : partecipation.getReplies())
      {
         if (reply.getNumberGathered() != null && !reply.getNumberGathered().trim().isEmpty()
                  && reply.getQuestion().isCorrect() == reply.getResponse())
         {
            reply.addPoints();
            total += reply.getPoints();
         }
      }
      partecipation.getQuizCompetitor().setScore(Long.valueOf(total));
      long totalTime = partecipation.getDateEnd().getTime() - partecipation.getDateStart().getTime();
      partecipation.getQuizCompetitor().setTotalTime(totalTime);
   }

   public static void main(String[] args)
   {
      List<String> stringhe = new ArrayList<String>();
      LinkedList<Integer> list = new LinkedList<Integer>();
      for (int i = 0; i < 10; i++)
      {
         list.add(i);
         stringhe.add("ciao" + i);
      }
      Collections.shuffle(list);
      for (int i = 0; i < 20; i++)
      {
         logger.info("" + i);
         if (!list.isEmpty())
         {
            int l = list.remove();
            logger.info(stringhe.get(l));
         }
      }
   }
}

package kr.bydelta.koala.test.pack

import java.io.{File, FileInputStream, FileOutputStream}

import com.twitter.chill.{Input, Output}
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.{FunctionalTag, POS, PhraseType}
import kr.bydelta.koala.data._
import kr.bydelta.koala.kryo.{DictionaryIO, DictionaryStream, KryoWrap}
import kr.bydelta.koala.traits.CanCompileDict
import org.specs2.mutable.Specification

import scala.collection.mutable
import scala.language.reflectiveCalls

/**
  * Created by bydelta on 16. 8. 5.
  */
object KryoSpec extends Specification {
  sequential

  val sent = Sentence(
    Seq(
      Word(0,
        "흰",
        Seq(Morpheme(0, "희", "VA", POS.VA), Morpheme(1, "ㄴ", "ETM", POS.ETM))
      ),
      Word(1,
        "밥을",
        Seq(Morpheme(0, "밥", "NNG", POS.NNG), Morpheme(1, "을", "JKO", POS.JKO))
      ),
      Word(2,
        "나는",
        Seq(Morpheme(0, "나", "NP", POS.NP), Morpheme(1, "는", "JX", POS.JX))
      ),
      Word(3,
        "먹었다",
        Seq(Morpheme(0, "먹", "VV", POS.VV), Morpheme(1, "었", "EP", POS.EP), Morpheme(2, "다", "EF", POS.EF))
      )
    )
  )

  sent withDependency Dependency(
    PhraseType.VP, FunctionalTag.ROOT, sent(3),
    Dependency(PhraseType.NP, FunctionalTag.OBJ, sent(1),
      Dependency(PhraseType.DP, FunctionalTag.MOD, sent(0))
    ),
    Dependency(PhraseType.NP, FunctionalTag.SBJ, sent(2))
  )

  sent withPhraseTree Phrase(
    PhraseType.S,
    Phrase(PhraseType.NP,
      Phrase(PhraseType.DP, sent(0)),
      Phrase(PhraseType.NP, sent(1)),
    ),
    Phrase(PhraseType.NP, sent(2)),
    Phrase(PhraseType.VP, sent(3))
  )

  val Dictionary = new CanCompileDict {
    val words = mutable.Set.empty[(String, POSTag)]
    override def addUserDictionary(dict: (String, POSTag)*): Unit =
      dict.foreach(words.add)

    override def items: Set[(String, POSTag)] = words.toSet

    override def baseEntriesOf(filter: POSTag => Boolean): Iterator[(String, POSTag)] =
      words.filter(x => filter(x._2)).iterator

    override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] =
      word.diff(words.toSeq)
  }


  "SentenceSerializer" should {
    "save a sentence" in {
      val kryo = KryoWrap.kryo

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test.sent")
      val output = new Output(new FileOutputStream(tmpFile))
      kryo.writeObject(output, sent)
      output.close()

      val input = new Input(new FileInputStream(tmpFile))
      val sent2 = kryo.readObject(input, classOf[Sentence])
      input.close()

      sent.singleLineString must_== sent2.singleLineString
      sent.dependencies must_== sent2.dependencies
      sent.phrases must_== sent2.phrases
    }
  }

  "KryoSerializer" should {
    "save and load dictionary by operator-like methods" in {
      // Clean up
      Dictionary.words.clear()

      Dictionary.addUserDictionary("힐스테이트1", POS.NNP)

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test.dic")
      Dictionary >> tmpFile

      Dictionary.words.clear()

      Dictionary.words must beEmpty
      Dictionary << tmpFile

      Dictionary.words must contain("힐스테이트1" -> POS.NNP)
    }

    "save and load dictionary by methods" in {
      // Clean up
      Dictionary.words.clear()

      Dictionary.addUserDictionary("힐스테이트0", POS.NNP)

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test0.dic")
      Dictionary.saveTo(tmpFile)

      Dictionary.words.clear()

      Dictionary.words must beEmpty
      Dictionary.readFrom(tmpFile)

      Dictionary.words must contain("힐스테이트1" -> POS.NNP)

    }
  }

  "DictionaryStream" should {
    "save and load dictionary" in {
      Dictionary.words.clear()
      Dictionary.addUserDictionary("힐스테이트2", POS.NNP)

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test-java.dic")
      val dicStream = new DictionaryStream(Dictionary)
      dicStream saveTo tmpFile

      Dictionary.words.clear()

      Dictionary.words must beEmpty
      dicStream readFrom tmpFile

      Dictionary.words must contain("힐스테이트2" -> POS.NNP)
    }
  }
}

package kr.bydelta.koala.kryo

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import kr.bydelta.koala.{FunctionalTag, PhraseType}
import kr.bydelta.koala.data._

/**
  * KryoSerializer object for Sentence class
  */
object SentenceSerializer extends Serializer[Sentence] {
  override def write(kryo: Kryo, output: Output, value: Sentence): Unit = {
    output.writeShort(value.size)
    value.words.foreach(kryo.writeObject(output, _))

    output.writeBoolean(value.phrases.isDefined)
    if(value.phrases.isDefined) {
      kryo.writeObject(output, value.phrases.get)
    }

    output.writeBoolean(value.dependencies.isDefined)
    if(value.dependencies.isDefined) {
      kryo.writeObject(output, value.dependencies.get)
    }
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Sentence]): Sentence = {
    val szWords = input.readShort
    val words = (0 until szWords).map {
      _ => kryo.readObject(input, classOf[Word])
    }

    val sent = Sentence(words)

    if(input.readBoolean())
      sent withPhraseTree kryo.readObject(input, classOf[Phrase])

    if(input.readBoolean())
      sent withDependency kryo.readObject(input, classOf[Dependency])

    sent
  }
}

object PhraseSerializer extends Serializer[Phrase]{
  override def write(kryo: Kryo, output: Output, value: Phrase): Unit = {
    output.writeByte(value.typeName.id)
    output.writeByte(value.size)
    value.foreach(kryo.writeClassAndObject(output, _))
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Phrase]): Phrase = {
    val typeId = PhraseType(input.readByte())
    val size = input.readByte()
    val children = (0 until size).map(_ => kryo.readClassAndObject(input).asInstanceOf[SyntacticPhraseLike])
    Phrase(typeId, children:_*)
  }
}

object WordPhraseSerializer extends Serializer[WordPhrase]{
  override def write(kryo: Kryo, output: Output, value: WordPhrase): Unit = {
    output.writeByte(value.typeName.id)
    kryo.writeObject(output, value.word)
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[WordPhrase]): WordPhrase = {
    val typeId = PhraseType(input.readByte())
    Phrase(typeId, kryo.readObject(input, classOf[Word]))
  }
}

object DependencySerializer extends Serializer[Dependency]{
  override def write(kryo: Kryo, output: Output, value: Dependency): Unit = {
    output.writeByte(value.function.id)
    output.writeByte(value.typeName.id)
    kryo.writeObject(output, value.word)
    output.writeByte(value.size)
    value.foreach(kryo.writeClassAndObject(output, _))
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Dependency]): Dependency = {
    val function = FunctionalTag(input.readByte())
    val phrasetag = PhraseType(input.readByte())
    val word = kryo.readObject(input, classOf[Word])
    val size = input.readByte()
    val children = (0 until size).map(_ => kryo.readClassAndObject(input).asInstanceOf[DependencyRelationLike])
    Dependency(phrasetag, function, word, children:_*)
  }
}

object WordDependencySerializer extends Serializer[WordDependency]{
  override def write(kryo: Kryo, output: Output, value: WordDependency): Unit = {
    output.writeByte(value.function.id)
    output.writeByte(value.typeName.id)
    kryo.writeObject(output, value.word)
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[WordDependency]): WordDependency = {
    val function = FunctionalTag(input.readByte())
    val phrasetag = PhraseType(input.readByte())
    Dependency(phrasetag, function, kryo.readObject(input, classOf[Word]))
  }
}
package kr.bydelta.koala.kryo

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import kr.bydelta.koala.data.{Morpheme, Word}

/**
  * KryoSerializer object for Word class
  */
object WordSerializer extends Serializer[Word] {
  override def write(kryo: Kryo, output: Output, value: Word): Unit = {
    output.writeByte(value.id)
    output.writeString(value.surface)
    output.writeShort(value.size)
    value.morphemes.foreach(kryo.writeObject(output, _))
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Word]): Word = {
    val id = input.readByte()
    val surface = input.readString
    val szMorph = input.readShort()
    val morphs = (0 until szMorph).map {
      _ => kryo.readObject(input, classOf[Morpheme])
    }

    Word(id, surface, morphs)
  }
}

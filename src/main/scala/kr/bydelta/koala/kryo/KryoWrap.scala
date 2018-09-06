package kr.bydelta.koala.kryo

import com.twitter.chill.{KryoBase, ScalaKryoInstantiator}
import kr.bydelta.koala.data._

/**
  * An Kryo Instantiator using ScalaKryoInstantiator.
  */
object KryoWrap {
  private val instantiator = new ScalaKryoInstantiator

  /**
    * Instantiate Kryo instance which serializers are registered in.
    *
    * @return Kryo instance.
    */
  def kryo: KryoBase = {
    val k = instantiator.newKryo()
    k.addDefaultSerializer(classOf[Morpheme], MorphemeSerializer)
    k.addDefaultSerializer(classOf[Word], WordSerializer)
    k.addDefaultSerializer(classOf[Sentence], SentenceSerializer)
    k.addDefaultSerializer(classOf[Phrase], PhraseSerializer)
    k.addDefaultSerializer(classOf[WordPhrase], WordPhraseSerializer)
    k.addDefaultSerializer(classOf[Dependency], DependencySerializer)
    k.addDefaultSerializer(classOf[WordDependency], WordDependencySerializer)
    k
  }
}

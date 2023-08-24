package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository

class TracksInteractorImpl(private  val repositiry: TracksRepository): TracksInteractor {

    override fun getMusic(term: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread{
            consumer.consume(repositiry.getMusic(term))
        }
        t.start()
    }
}
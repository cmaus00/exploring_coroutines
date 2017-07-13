@file:JvmName("Main")

package com.example.exploring_coroutines

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.selects.select

suspend fun numberCrunch(x: Int): Int {
    println("starting numberCrunch " + x)
    for (i in x..10) {
        delay(200)
    }
    println("ending numberCrunch " + x)
    return x
}


suspend fun <T> Collection<Deferred<T>>.firstResolved(cancel : Boolean = true) : Deferred<T> = select {
    forEach { d ->
        d.onAwait {
            if (cancel) {
                forEach { d2 -> d2.cancel() }
            }
            d
        }
    }
}

suspend fun <T> Collection<Deferred<T>>.awaitFirstResolved(cancel: Boolean = true) : T = firstResolved(cancel).await()


/**
 * Created by c.maus on 27.06.17.
 */
fun main(args: Array<String>) = runBlocking(Unconfined) {
    val coroutineContext = newSingleThreadContext("numberCrunching")

    launch(coroutineContext) {
        println("start sync")

        val number1 = async(coroutineContext) {
            numberCrunch(1)
        }

        val number2 = async(coroutineContext) {
            numberCrunch(2)
        }



        val result = listOf(number1, number2).awaitFirstResolved()
        println(result)
        delay(400)
        println(number1.isActive)
    }.join()


    println("end sync")
}

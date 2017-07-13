package com.example.exploring_coroutines

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.BehaviorSpec
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.runBlocking

/**
 * Created by c.maus on 28.06.17.
 */
class DeferredTest : BehaviorSpec() {
    init {
        Given("a list with two deferreds") {
            val context = newSingleThreadContext("testContext")
            val def1 = async(context) {
                delay(200)
                1
            }

            val def2 = async(context) {
                delay(50)
                2
            }

            val list = listOf(
                    def1,
                    def2
            )

            WhenSync("firstResolved is called") {
                val result = list.awaitFirstResolved()
                Then("the returned value should be the one of the first completed deferred") {
                    result shouldEqual 2
                }
            }
        }
    }
}

fun BehaviorSpec.Given.WhenSync(description: String, init: suspend BehaviorSpec.When.() -> Unit) = When(description, {
    runBlocking {
        init()
    }
})




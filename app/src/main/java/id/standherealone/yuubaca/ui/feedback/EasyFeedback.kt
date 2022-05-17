package id.standherealone.yuubaca.ui.feedback

import android.content.Context
import android.content.Intent

class EasyFeedback(builder: Builder) {
    private val context: Context
    private val emailId: String?
    private val withSystemInfo: Boolean

    class Builder(val context: Context) {
        var emailId: String? = null
        var withSystemInfo = false
        fun withEmail(email: String?): Builder {
            emailId = email
            return this
        }

        fun withSystemInfo(): Builder {
            withSystemInfo = true
            return this
        }

        fun build(): EasyFeedback {
            return EasyFeedback(this)
        }
    }

    fun start() {
        val intent = Intent(context, FeedbackActivity::class.java)
        intent.putExtra("email", emailId)
        intent.putExtra("with_info", withSystemInfo)
        context.startActivity(intent)
    }

    init {
        emailId = builder.emailId
        context = builder.context
        withSystemInfo = builder.withSystemInfo
    }
}
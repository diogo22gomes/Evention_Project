import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_JWT_TOKEN = "TOKEN"
        private const val KEY_USER_ID = "USER_ID"
        private const val KEY_NOTIFICATION_PERMISSION_SHOWN = "notification_permission_shown"
        private const val KEY_USERTYPE = "USERTYPE"

    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_JWT_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_JWT_TOKEN, null)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_JWT_TOKEN).apply()
    }

    fun saveUserId(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun clearUserId() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    fun setNotificationPermissionShown() {
        prefs.edit().putBoolean(KEY_NOTIFICATION_PERMISSION_SHOWN, true).apply()
    }

    fun isNotificationPermissionShown(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATION_PERMISSION_SHOWN, false)
    }

    fun saveUserType(userType: String) {
        prefs.edit().putString(KEY_USERTYPE, userType).apply()
    }

    fun getUserType(): String? {
        return prefs.getString(KEY_USERTYPE, null)
    }

}

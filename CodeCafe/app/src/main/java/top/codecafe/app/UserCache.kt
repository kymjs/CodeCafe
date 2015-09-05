package top.codecafe.app

import android.content.Context
import top.codecafe.app.bean.User
import top.codecafe.kjframe.utils.FileUtils
import java.io.*

/**
 * @author kymjs (http://www.kymjs.com/) on 15/8/21.
 */
public object UserCache {

    private var user: User? = null

    public fun isLogin(cxt: Context): Boolean {
        if (user == null) {
            user = read(getLoginCacheFile(cxt)) as User?
        }
        return user?.isLogin() ?: false
    }

    public fun getUser(cxt: Context): User {
        if (user == null) {
            user = read(getLoginCacheFile(cxt)) as User?
        }
        return user as User
    }

    public fun save(cxt: Context, user: User) {
        save(getLoginCacheFile(cxt), user)
    }

    public fun getLoginCacheFile(cxt: Context): File {
        val cacheDir = cxt.getCacheDir()
        val cacheFile = File("$cacheDir/cookie")
        if (!cacheFile.exists()) {
            try {
                cacheFile.createNewFile()
            } catch (e: IOException) {
            }
        }
        return cacheFile
    }

    public fun save(saveFile: File, o: Serializable) {
        var fos: FileOutputStream? = null
        var oos: ObjectOutputStream? = null

        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile()
            }

            fos = FileOutputStream(saveFile)
            oos = ObjectOutputStream(fos)
            oos.writeObject(o)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            FileUtils.closeIO(oos, fos)
        }
    }

    public fun read(saveFile: File): Any? {
        var fis: FileInputStream? = null
        var ois: ObjectInputStream? = null
        var obj: Any?
        try {
            fis = FileInputStream(saveFile)
            ois = ObjectInputStream(fis)
            obj = ois.readObject()
        } catch (e: Exception) {
            obj = null
        } finally {
            FileUtils.closeIO(ois, fis)
        }
        return obj
    }
}

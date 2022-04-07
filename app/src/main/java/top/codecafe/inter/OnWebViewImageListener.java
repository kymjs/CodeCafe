package top.codecafe.inter;

/**
 * 监听webview上的图片
 */
public interface OnWebViewImageListener {

	/**
	 * 点击webview上的图片，传入该缩略图的大图Url
	 * @param url 图片地址
	 */
	void showImagePreview(String url);
}

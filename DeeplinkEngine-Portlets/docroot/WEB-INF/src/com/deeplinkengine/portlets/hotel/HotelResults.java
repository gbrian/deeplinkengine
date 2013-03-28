package com.deeplinkengine.portlets.hotel;

import java.io.IOException;
import java.util.Map;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;
import javax.servlet.http.HttpServletRequest;

import com.deeplinkengine.common.SearchSettings;
import com.deeplinkengine.portlets.common.AsyncResultsCache;
import com.deeplinkengine.portlets.common.ProvidersLoader;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class HotelResults
 */
public class HotelResults extends MVCPortlet {

	static Object cacheSemaphore = new Object();

	@Override
	public void doEdit(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
		renderResponse.setContentType("text/html");
		PortletURL settings = renderResponse.createActionURL();
		settings.setParameter("settings", "settings");
		renderRequest.setAttribute("settingsUrl", settings.toString());
		renderRequest.setAttribute("providers", ProvidersLoader.getProviders());
		PortletPreferences prefs = renderRequest.getPreferences();
		renderRequest
				.setAttribute("provider", prefs.getValue("provider", null));

		include(this.editTemplate, renderRequest, renderResponse);
	}

	@Override
	public void processAction(javax.portlet.ActionRequest actionRequest,
			javax.portlet.ActionResponse actionResponse) throws IOException,
			PortletException {

		if (actionRequest.getParameter("settings") != null) {
			PortletPreferences prefs = actionRequest.getPreferences();
			prefs.setValue("provider", actionRequest.getParameter("provider"));
			prefs.store();
			actionResponse.setPortletMode(PortletMode.VIEW);
		}
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		// TODO Auto-generated method stub
		super.processEvent(request, response);
	}

	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		resourceResponse.setContentType("text/json");

		if (resourceRequest.getResourceID().equals("results")) {
			String token = HtmlUtil.escapeURL(resourceRequest.getParameter("token"));
			AsyncResultsCache res = (AsyncResultsCache) fromCache(token);
			if (res == null) {
				resourceResponse.getWriter().write("[]");
				return;
			} else if (res.getState() == AsyncResultsCache.STATUS.WAITING) {
				try {
					res.getWorker().join(50000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			resourceResponse.getWriter().write((String) res.getResults());
		}
		super.serveResource(resourceRequest, resourceResponse);
	}

	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
		HttpServletRequest httpReq = PortalUtil
				.getOriginalServletRequest(PortalUtil
						.getHttpServletRequest(renderRequest));
		Map<String, String[]> params = httpReq.getParameterMap();

		SearchSettings settings = null;
		try {
			// Load search!
			settings = new SearchSettings(params);
			
			PortletPreferences prefs = renderRequest.getPreferences();
			
			final String provider = prefs.getValue("provider", ProvidersLoader
					.getProviders().get(0));
			final String token = HtmlUtil.escapeURL(provider + settings.toString());
			
			addRequestToCache(provider, token, settings);
			// Add token!
			renderRequest.setAttribute("token", token);
			// Add ajax actionURL
			ResourceURL ajaxCall = renderResponse.createResourceURL();
			ajaxCall.setResourceID("results");
			renderRequest.setAttribute("resultsUrl", ajaxCall.toString());
			
		} catch (Exception e) {
			// FIXME: ....
		}
		super.doView(renderRequest, renderResponse);
	}

	/**
	 * 
	 * @param provider
	 * @param token
	 */
	protected void addRequestToCache(final String provider, final String token,
			final SearchSettings settings) {
		synchronized (cacheSemaphore) {
			AsyncResultsCache async = ((AsyncResultsCache) HotelResults
					.fromCache(token));
			if (async == null) {
				async = new AsyncResultsCache("searchToken", new Thread(
						new Runnable() {
							@Override
							public void run() {
								String res = null;

								res = ProvidersLoader.getProviderInstance(
										provider, null).doHotelSearch(settings,
										null);
								((AsyncResultsCache) HotelResults
										.fromCache(token)).setResults(res);
							}
						}));
				async.getWorker().start();
				toCache(token, async);
			}
		}
	}

	static final String _data = "[{\"img\":\"http://www.hotelbeds.com/giata/13/137952/137952a_hb_a_001.jpg\",\"id\":\"137952\",\"name\":\"Radisson Blu Madrid Prado\",\"desc\":\"Este hotel, con una ubicación increíble a solo unos pasos del Museo del Prado (a 100 m) y al lado del centro cultural Caixa Forum Madrid, es la personificación de la cultura y la sofisticación en el Triángulo de Oro o de Arte de Madrid. Dista 200 m de la estación de metro más cercana y 400 m del Corte Inglés. La estación de tren de Atocha está a 500 m y el aeropuerto de Madrid está a 14 km.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/02/023424/023424a_hb_a_001.jpg\",\"id\":\"23424\",\"name\":\"Gran Atlanta\",\"desc\":\"El hotel se ubica en una de las capitales más maravillosas de Europa. Tiene una cómoda ubicación para acceder al transporte público, con tres estaciones de metro a un corto paseo. Dichas líneas enlazan con el centro histórico de la ciudad, la Puerta del Sol, la Plaza Mayor y Nuevos Ministerios, en conexión con el aeropuerto de Madrid-Barajas. Los alrededores del hotel cuentan con variados restaurantes, bares y zonas comerciales para todas las necesidades de los huéspedes, además del estadio de fútbol del Real Madrid, el Santiago Bernabeu. El museo Thyssen-Bornemisza y el Museo del Prado están a unos 4 km.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/02/026196/026196a_hb_a_002.jpg\",\"id\":\"26196\",\"name\":\"Clement Barajas\",\"desc\":\"Este hotel se sitúa a 500 metros de un parque y a unos 12 km del centro urbano. Directamente enfrente del hotel hay paradas de transporte público y restaurantes. Hay también numerosos bares a unos 50 metros y el hotel está muy próximo del centro de congresos. El aeropuerto dista 1 km.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/005271/005271a_hb_a_002.jpg\",\"id\":\"5271\",\"name\":\"Petit Palace Avenida Gran V&#237;a\",\"desc\":\"Este hotel está ubicado en el centro de Madrid, en plena Gran Vía, a sólo 2 minutos a pie de la Puerta del Sol, la Plaza Mayor y el Palacio Real. Los clientes tienen al alcance de su mano tanto los museos (Prado, Reina Sofía y Thyssen) como los comercios más atractivos. Todo ello sin necesidad de medios de transporte.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/003020/003020a_hb_a_001.jpg\",\"id\":\"3020\",\"name\":\"Regente\",\"desc\":\"Este encantador hotel está en el centro de la ciudad de Madrid, a sólo unos pasos de la Gran Vía. Está rodeado de teatros, cines, restaurantes, bares y numerosos comercios y tiendas. Muy cerca del hotel encontrará servicio de transporte público (metro y autobús).\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/000569/000569a_hb_a_001.jpg\",\"id\":\"569\",\"name\":\"Emperador\",\"desc\":\"Este lujoso hotel es uno de los preferidos por encontrarse en una ubicación central en el corazón de Madrid, en la conocida Gran Vía. Está rodeado de tiendas, bancos, boutiques de moda, cines, teatros y discotecas. También queda cerca del centro histórico de la ciudad, con el Palacio Real, la ópera, la Puerta del Sol, la Cibeles y los muesos de Reina Sofía, Thyssen y el Prado. Justo a la puerta hay una parada de transporte público con autobuses y la parada de metro Santo Domingo.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/003549/003549a_hb_a_001.jpg\",\"id\":\"3549\",\"name\":\"Palacio San Martin\",\"desc\":\"Este hotel de lujo, en un edificio clásico del siglo XIX, está situado en el corazón de Madrid, a sólo unos pasos de la Gran Vía y del Palacio Real. Podrá llegar a pie a diversos restaurantes ( unos 100 m), bares y discotecas ( unos 100m) y tiendas (50 m), así como a una parada de transporte público ( a unos 250 m).\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/001233/001233a_hb_a_001.jpg\",\"id\":\"1233\",\"name\":\"Ayre Gran Hotel Colon\",\"desc\":\"Este hotel de lujo está situado en el animado corazón de la ciudad, cerca del Parque del Retiro. Este hotel proporciona la base ideal para quienes deseen explorar las numerosas tiendas y locales culturales de la metrópoli, así como sus principales monumentos.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/12/122649/122649a_hb_w_001.jpg\",\"id\":\"122649\",\"name\":\"Ada Palace\",\"desc\":\"El hotel se halla en la zona más noble de la Gran Vía, cerca de la calle de Alcalá y a un paso de la plaza de la Cibeles, del Banco de España y del Ayuntamiento. Además, está perfectamente comunicado gracias a las varias líneas de metro y de autobús, y queda a escasos metros de la estación de Recoletos. Está rodeado por muchos atractivos turísticos y monumentos de la ciudad. El aeropuerto internacional de Barajas (Madrid) dista 22 km aproximadamente.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/004303/004303a_hb_a_001.jpg\",\"id\":\"4303\",\"name\":\"Auditorium Madrid\",\"desc\":\"Este hotel de conferencias, único en su tipo en Madrid, le ofrece la efectividad de un gran centro de convenciones y, al mismo tiempo, todo el confort de un complejo de primera categoría.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/001236/001236a_hb_a_001.jpg\",\"id\":\"1236\",\"name\":\"Mayorazgo\",\"desc\":\"Este elegante hotel de lujo está situado en el corazón de Madrid, en la Gran Vía. En las cercanías hay muchas tiendas, comercios y lugares de ocio y de entretenimiento. A pocos minutos se encuentran el Palacio Real, la Plaza de España y la Plaza Mayor.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/001219/001219a_hb_a_001.jpg\",\"id\":\"1219\",\"name\":\"Tryp Menfis\",\"desc\":\"Este hotel está en el centro de la ciudad. Se encuentra cercano al lugar donde los musicales, el shopping, la gastronomía y los principales centros culturales se mezclan para proporcionar una calle única por su vitalidad: La Gran Vía. El hotel está localizado cerca de de lugares como la Plaza de España, la Puerta del Sol y del Triangulo del Arte: el Museo del Prado, el Museo Reina Sofía y el Museo Thyssen-Bornemisza.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/07/078412/078412a_hb_a_001.jpg\",\"id\":\"78412\",\"name\":\"Exe Suites 33\",\"desc\":\"Este hotel se encuentra en el Barrio de Palacio, en el centro de Madrid, a menos de 5 minutos de los lugares más importantes y centros culturales, así como del Palacio Real, la Ópera, el Senado, Plaza de Callao, Calle Preciados, Puerta del Sol, Plaza de España, Gran Vía y la Plaza Mayor. Asímismo, hay multitud de comercios en las cercanías. Los alrededores de Madrid incluyen también preciosas zonas naturales, montañas en el norte y en la misma ciudad, así como varios lugares históricos como El Escorial o Alcalá de Henares.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/001225/001225a_hb_a_009.jpg\",\"id\":\"1225\",\"name\":\"Exe Coloso\",\"desc\":\"Este hotel está situado en el centro de la magnífica metrópoli madrileña, a pocos minutos andando del Palacio Real y de la Plaza Mayor. Gracias a su cercana ubicación a la Gran Vía, es un punto ideal para salir de compras y de marcha por la noche. A solamente 100 metros de distancia tiene varias paradas de transporte público. El aeropuerto de Barajas está a una distancia aproximada de 15 kilómetros.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/00/001224/001224a_hb_a_001.jpg\",\"id\":\"1224\",\"name\":\"Vincci Capitol\",\"desc\":\"Este hotel está situado en el corazón de la capital española, cerca de la Puerta del Sol y de la Plaza Mayor. Se encuentra en medio del centro de negocios y cerca de los museos de El Prado, Reina Sofia y Thyssen Bornemitza.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/14/141697/141697a_hb_a_001.jpg\",\"id\":\"141697\",\"name\":\"Apartasuites Satellite by Abalu hoteles\",\"desc\":\"Los apartamentos se encuentran en el corazón de Madrid, a un paso de Plaza España. La estación de metro Noviciado está a 200 m y ofrece conexiones excelentes con todas las partes de la ciudad, y hay restaurantes, bares, tiendas y clubs en las inmediaciones. El centro histórico de la ciudad dista unos 250 m y hay museos a unos 500 m del hotel. El aeropuerto de Madrid Barajas dista unos 15 km.\\n            +Info\"},{\"img\":\"http://www.hotelbeds.com/giata/15/154274/154274a_hb_a_001.jpg\",\"id\":\"154274\",\"name\":\"Splendom Suites Madrid\",\"desc\":\"Más información de los hoteles dentro de poco tiempo.\\n            +Info\"}]";

	static public void toCache(String key, Object value) {
		MultiVMPool pool = MultiVMPoolUtil.getMultiVMPool();
		PortalCache cache = pool.getCache("results");
		cache.put(key, value, 50000);
	}

	static public Object fromCache(String key) {

		MultiVMPool pool = MultiVMPoolUtil.getMultiVMPool();
		PortalCache cache = pool.getCache("results");
		return cache.get(key);
	}
}

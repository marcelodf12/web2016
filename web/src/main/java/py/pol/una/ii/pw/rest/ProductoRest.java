package py.pol.una.ii.pw.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.service.ProductoEjb;
import py.pol.una.ii.pw.util.Respuesta;

@Path("/producto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoRest {

	@Inject
	private ProductoEjb productoEjb;

	@GET
	@Path("/{id:[0-9][0-9]*}")
	public Respuesta<Producto> buscar(@PathParam("id") Long id) {
		return productoEjb.buscarPorId(id);
	}

	@GET
	public Respuesta<List<Producto>> buscarTodos() {
		return productoEjb.listarTodos();
	}

	@POST
	public Respuesta<Producto> alta(Producto p) {
		return productoEjb.nuevo(p);
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	public Respuesta<Producto> modificar(@PathParam("id") Long id, Producto p) {
		return productoEjb.modificar(id, p);
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Respuesta<Producto> baja(@PathParam("id") Long id) {
		return productoEjb.eliminar(id);
	}

	private final String UPLOADED_FILE_PATH = "c:\\home\\";
	
	@POST
	@Path("/file")
	@Consumes("multipart/form-data")
	public Respuesta<String> uploadFile(MultipartFormDataInput input) throws URISyntaxException {

		String fileName = "";
		Respuesta<String> r = null;
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");

		for (InputPart inputPart : inputParts) {

			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				byte[] bytes = IOUtils.toByteArray(inputStream);

				// constructs upload file path
				fileName = UPLOADED_FILE_PATH + fileName;

				writeFile(bytes, fileName);

				r = productoEjb.cargaMasiva(fileName);

			} catch (IOException e) {
				r = new Respuesta<String>();
				r.setSuccess(false);
				r.setMessages("IOException");
				r.setReason(e.getMessage());
				e.printStackTrace();
			}

		}
		return r;
	}

	/**
	 * header sample { Content-Type=[image/png], Content-Disposition=[form-data;
	 * name="file"; filename="filename.extension"] }
	 **/
	// get uploaded filename, is there a easy way in RESTEasy?
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	// save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}

package py.pol.una.ii.pw.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import py.pol.una.ii.pw.dto.ProductoDto;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.service.ProductoEjb;
import py.pol.una.ii.pw.service.ProductoEjbStateful;
import py.pol.una.ii.pw.util.Pagina;
import py.pol.una.ii.pw.util.Respuesta;

@Path("/producto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoRest {

	@Inject
	private ProductoEjb productoEjb;
	
	@EJB
	ProductoEjbStateful productoEjbStateful;

	@GET
	@Path("/{id:[0-9][0-9]*}")
	public Respuesta<Producto> buscar(@PathParam("id") Long id) {
		return productoEjb.buscarPorId(id);
	}

	@GET
	@Path("/pagina")
	public Respuesta<Pagina<Producto>> buscar(@QueryParam("inicio") Integer inicio, @QueryParam("cantidad") Integer cant) {
		return productoEjb.listarTodos(inicio, cant);
	}
	
	@GET
	@Produces("multipart/form-data")
	public MultipartFormDataOutput  buscarTodos(){
		MultipartFormDataOutput output = new MultipartFormDataOutput();
		productoEjbStateful.iniciar();
		while(productoEjbStateful.hasNext()){
			Producto p = productoEjbStateful.nextProducto();
			if(p!=null){
				ProductoDto p1 = new ProductoDto(p);
				output.addPart(p1.cad(), MediaType.APPLICATION_XML_TYPE);
			}
		}
		System.out.println("***********");
		productoEjbStateful.terminar();
		return output;
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
			} catch ( Exception e) {
				r = new Respuesta<String>();
				r.setSuccess(false);
				r.setMessages("Exception");
				r.setReason(e.getMessage());
				e.printStackTrace();
			}

		}
		return r;
	}

	/**
	 * header { Content-Type=[image/png], Content-Disposition=[form-data;
	 * name="file"; filename="filename.extension"] }
	 **/
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		Random rnd = new Random();
		Integer ram = new Integer((int) (rnd.nextDouble()*1000+1));
		
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				
				
				return finalFileName + ram.toString() + ".txt";
			}
		}
		return "unknown" + ram.toString() + ".txt";
	}

	
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

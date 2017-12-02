package cs636.music.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cs636.music.dao.AdminDAO;
import cs636.music.dao.DbDAO;
import cs636.music.dao.DownloadDAO;
import cs636.music.dao.InvoiceDAO;
import cs636.music.dao.LineItemDAO;
import cs636.music.dao.UserDAO;
import cs636.music.domain.Download;
import cs636.music.domain.Invoice;
import cs636.music.domain.User;
import cs636.music.service.data.DownloadData;
import cs636.music.service.data.InvoiceData;

public class AdminService {

	private DbDAO db;
	private AdminDAO adminDb;
	private UserDAO userdb;
	private InvoiceDAO invoicedb;
	private DownloadDAO downloaddb;
	private LineItemDAO lineItemdb;

	/**
	 * construct a admin service provider
	 * 
	 * @param dbDao
	 */
	public AdminService(DbDAO dbDao, AdminDAO adminDao, DownloadDAO downloadDao, LineItemDAO lineItemDao,
			InvoiceDAO invoiceDao) {
		db = dbDao;
		adminDb = adminDao;
		downloaddb = downloadDao;
		lineItemdb = lineItemDao;
		invoicedb = invoiceDao;
	}

	/**
	 * Clean all user table, not product and system table to empty and then set
	 * the index numbers back to 1
	 * 
	 * @throws ServiceException
	 */
	public void initializeDB() throws ServiceException {
		try {
			db.initializeDb();
		} catch (SQLException e) {
			throw new ServiceException("Can't initialize DB: ", e);
		}
	}

	/**
	 * process the invoice
	 * 
	 * @param invoiceId
	 * @throws ServiceException
	 */
	public void processInvoice(long invoiceId) throws IOException, ServiceException {
		try {
			invoicedb.updateInvoiceByID(invoiceId);
		} catch (Exception e) {
			throw new ServiceException("Invoice status does not changed...!!!", e);
		}
	}

	/**
	 * Get a list of all invoices
	 * 
	 * @return list of all invoices in InvoiceData objects
	 * @throws ServiceException
	 */
	public Set<InvoiceData> getListofInvoices() throws ServiceException {
		Set<Invoice> invoices = new HashSet<Invoice>();
		Set<InvoiceData> invoiceData = new HashSet<InvoiceData>();

		try {
			invoices = invoicedb.findAllInvoices();

			for (Invoice inv : invoices) {
				invoiceData.add(new InvoiceData(inv));
			}
		} catch (Exception e) {
			throw new ServiceException("Invoice search gives error!!!", e);
		}

		return invoiceData;
	}

	/**
	 * Get a list of all unprocessed invoices
	 * 
	 * @return list of all unprocessed invoices in InvoiceData objects
	 * @throws ServiceException
	 */
	public Set<InvoiceData> getListofUnprocessedInvoices() throws ServiceException {
		Set<Invoice> invoices = new HashSet<Invoice>();
		Set<InvoiceData> invoiceData = new HashSet<InvoiceData>();

		try {
			invoices = invoicedb.findAllUnprocessedInvoices();

			for (Invoice inv : invoices) {
				invoiceData.add(new InvoiceData(inv));
			}
		} catch (Exception e) {
			throw new ServiceException("unprocess Invoice search gives error!!!", e);
		}

		return invoiceData;
	}

	/**
	 * Get a list of all downloads
	 * 
	 * @return list of all downloads
	 * @throws ServiceException
	 */
	public Set<DownloadData> getListofDownloads() throws ServiceException {
		Set<DownloadData> downloadData = new HashSet<DownloadData>();
		try {
			Set<Download> download = downloaddb.findAllDownloads();

			for (Download d : download) {
				downloadData.add(new DownloadData(d));
			}

		} catch (Exception e) {
			throw new ServiceException("Find all downloads gives error!!!", e);
		}

		return downloadData;
	}

	/**
	 * Check login user
	 * 
	 * @param username
	 * @param password
	 * @return true if useranme and password exist, otherwise return false
	 * @throws ServiceException
	 */
	public Boolean checkLogin(String username, String password) throws ServiceException {
		try {
			return adminDb.findAdminUser(username, password);
		} catch (SQLException e) {
			throw new ServiceException("Check login error: ", e);
		}
	}

	public Set<DownloadData> findDownloadusingUseridAndTracId(long userId, long trackId)
			throws IOException, ServiceException {
		Set<Download> downloads;
		Set<DownloadData> downloadData = new HashSet<DownloadData>();

		try {
			downloads = downloaddb.findDownloadsByUserIDandTrackID(userId, trackId);

			for (Download d : downloads) {
				downloadData.add(new DownloadData(d));
			}

		} catch (Exception e) {
			throw new ServiceException("download data has error!!!", e);
		}

		return downloadData;

	}

	// public Invoice findInvoiceusingId(long invoiceId) throws IOException,
	// ServiceException{
	// Invoice invoice;
	// try {
	// invoice = invoicedb.findInvoice(invoiceId);
	// }
	// catch(Exception e) {
	// throw new ServiceException("Error at Invoice Search !!!",
	// e);
	// }
	// return invoice;
	// }

}

package org.openntf.nsfodp.notesapi.darwinonapi;

import java.io.IOException;
import java.io.OutputStream;

import org.openntf.nsfodp.commons.odp.notesapi.NCompositeData;
import org.openntf.nsfodp.commons.odp.notesapi.NDominoException;

import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.item.NSFCompositeData;
import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.NotesDatabase;
import com.ibm.designer.domino.napi.NotesNote;
import com.ibm.designer.domino.napi.NotesSession;
import com.ibm.designer.domino.napi.design.FileAccess;

public class DarwinoNCompositeData implements NCompositeData {
	private final NSFNote note;
	private final NSFCompositeData data;
	
	public DarwinoNCompositeData(NSFNote note, NSFCompositeData data) {
		this.note = note;
		this.data = data;
	}
	
	@Override
	public void writeFileResourceData(OutputStream os) throws IOException {
		try {
			this.data.writeFileResourceData(os);
		} catch (DominoException e) {
			throw new NDominoException(e.getStatus(), e);
		}
	}
	
	@Override
	public void writeImageResourceData(OutputStream os) throws IOException {
		try {
			this.data.writeImageResourceData(os);
		} catch (DominoException e) {
			throw new NDominoException(e.getStatus(), e);
		}
	}
	
	@Override
	public void writeJavaScriptLibraryData(OutputStream os) throws IOException {
		try {
			// TODO figure out why the Darwino implementation chops data
			//    https://github.com/OpenNTF/org.openntf.nsfodp/issues/271
			
//			this.data.writeJavaScriptLibraryData(os);
			
			long dbHandle = this.note.getParent().getHandle();
			NotesSession notesSession = new NotesSession();
			try {
				NotesDatabase notesDatabase = notesSession.getDatabase((int)dbHandle);
				NotesNote notesNote = notesDatabase.openNote(this.note.getNoteID(), 0);
				FileAccess.readFileContent(notesNote, os);
			} finally {
				notesSession.recycle();
			}
		} catch (NotesAPIException e) {
			throw new NDominoException(e.getNativeErrorCode(), e);
		} catch(DominoException e) {
			throw new NDominoException(e.getStatus(), e);
		}
	}

	@Override
	public void close() {
		this.data.free();
	}

}

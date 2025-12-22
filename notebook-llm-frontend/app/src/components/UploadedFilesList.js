import React from 'react';
import '../css/UploadedFilesList.css';

function UploadedFilesList({ files }) {
  if (files.length === 0) {
    return (
      <div className="uploaded-files">
        <h3>📚 Documents disponibles</h3>
        <div className="no-files">
          <p>Aucun document PDF uploadé</p>
          <p>Uploader des PDFs pour améliorer les réponses de l'assistant</p>
        </div>
      </div>
    );
  }

  return (
    <div className="uploaded-files">
      <h3>📚 Documents disponibles ({files.length})</h3>
      <div className="files-grid">
        {files.map((file, index) => (
          <div key={index} className="file-card">
            <div className="file-icon">📄</div>
            <div className="file-info">
              <div className="file-name">{file.name}</div>
              <div className="file-meta">
                {file.size && (
                  <span className="file-size">
                    {(file.size / 1024 / 1024).toFixed(2)} MB
                  </span>
                )}
                <span className="file-type">PDF</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default UploadedFilesList;
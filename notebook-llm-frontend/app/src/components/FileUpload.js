import React, { useState, useRef } from 'react';
import '../css/FileUpload.css';

function FileUpload({ onUpload, onBack }) {
  const [files, setFiles] = useState([]);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadMessage, setUploadMessage] = useState('');
  const fileInputRef = useRef(null);

  const handleFileChange = (e) => {
    const selectedFiles = Array.from(e.target.files);
    // Filtrer seulement les PDFs
    const pdfFiles = selectedFiles.filter(file => 
      file.type === 'application/pdf' || file.name.toLowerCase().endsWith('.pdf')
    );
    
    if (pdfFiles.length !== selectedFiles.length) {
      alert('Seuls les fichiers PDF sont acceptés !');
    }
    
    setFiles(pdfFiles);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (files.length === 0) {
      alert('Veuillez sélectionner au moins un fichier PDF');
      return;
    }

    setIsUploading(true);
    setUploadMessage('');

    try {
      await onUpload(files);
      setUploadMessage('✅ Fichiers uploadés avec succès !');
      setFiles([]);
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
      
      // Redirection automatique après 2 secondes
      setTimeout(() => {
        onBack();
      }, 2000);
    } catch (error) {
      setUploadMessage('❌ Erreur lors de l\'upload : ' + error.message);
    } finally {
      setIsUploading(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    const droppedFiles = Array.from(e.dataTransfer.files);
    const pdfFiles = droppedFiles.filter(file => 
      file.type === 'application/pdf' || file.name.toLowerCase().endsWith('.pdf')
    );
    setFiles(prev => [...prev, ...pdfFiles]);
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const removeFile = (index) => {
    setFiles(files.filter((_, i) => i !== index));
  };

  return (
    <div className="file-upload">
      <button onClick={onBack} className="back-button">
        ← Retour au chat
      </button>

      <h2>Upload de documents PDF</h2>
      <p className="upload-description">
        Ajoutez des documents PDF pour enrichir les connaissances de l'assistant
      </p>

      <div 
        className="drop-zone"
        onDrop={handleDrop}
        onDragOver={handleDragOver}
      >
        <input
          ref={fileInputRef}
          type="file"
          multiple
          accept=".pdf,application/pdf"
          onChange={handleFileChange}
          id="file-input"
          style={{ display: 'none' }}
        />
        
        <label htmlFor="file-input" className="drop-zone-label">
          <div className="upload-icon">📁</div>
          <p>Cliquez pour sélectionner ou glissez-déposez vos fichiers PDF</p>
          <p className="file-hint">Fichiers PDF uniquement (taille max: 10MB par fichier)</p>
          <button type="button" className="browse-button">
            Parcourir les fichiers
          </button>
        </label>
      </div>

      {files.length > 0 && (
        <div className="selected-files">
          <h4>Fichiers sélectionnés ({files.length}) :</h4>
          <ul>
            {files.map((file, index) => (
              <li key={index} className="file-item">
                <span className="file-name">{file.name}</span>
                <span className="file-size">
                  ({(file.size / 1024 / 1024).toFixed(2)} MB)
                </span>
                <button 
                  onClick={() => removeFile(index)}
                  className="remove-file"
                >
                  ×
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}

      <button
        onClick={handleSubmit}
        disabled={files.length === 0 || isUploading}
        className="upload-button"
      >
        {isUploading ? 'Upload en cours...' : `Uploader ${files.length} fichier(s)`}
      </button>

      {uploadMessage && (
        <div className={`upload-message ${uploadMessage.includes('✅') ? 'success' : 'error'}`}>
          {uploadMessage}
        </div>
      )}
    </div>
  );
}

export default FileUpload;
import React, { useState, useEffect } from 'react';
import { chatAPI } from './services/apiService';
import Header from './components/Header';
import ChatHistory from './components/ChatHistory';
import ChatInterface from './components/ChatInterface';
import FileUpload from './components/FileUpload';
import UploadedFilesList from './components/UploadedFilesList';
import './css/App.css';

function App() {
  const [activeTab, setActiveTab] = useState('chat');
  const [conversations, setConversations] = useState([]);
  const [uploadedFiles, setUploadedFiles] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingHistory, setIsLoadingHistory] = useState(true);

  // Charger l'historique au démarrage
  useEffect(() => {
    loadConversations();
  }, []);

  const loadConversations = async () => {
    try {
      setIsLoadingHistory(true);
      const data = await chatAPI.getConversations();
      setConversations(data);
    } catch (error) {
      console.error('Failed to load conversations:', error);
    } finally {
      setIsLoadingHistory(false);
    }
  };

  const handleAskQuestion = async (question) => {
    try {
      setIsLoading(true);
      const response = await chatAPI.askQuestion(question);
      
      // Recharger l'historique pour avoir les dernières conversations
      await loadConversations();
      
      // Vérifier si la réponse indique qu'aucune information n'a été trouvée
      if (response === "No response available for your question") {
        alert("L'assistant n'a pas trouvé d'informations pertinentes dans vos documents pour répondre à cette question.");
      }
    } catch (error) {
      console.error('Error asking question:', error);
      alert('Une erreur est survenue. Veuillez réessayer.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleUploadFiles = async (files) => {
    await chatAPI.uploadPDFs(files);
    
    // Ajouter les fichiers uploadés à la liste
    const newFiles = files.map(file => ({
      name: file.name,
      size: file.size,
      type: 'pdf',
      uploadDate: new Date().toLocaleDateString()
    }));
    
    setUploadedFiles(prev => [...prev, ...newFiles]);
  };

  return (
    <div className="app">
      <Header 
        activeTab={activeTab}
        onTabChange={setActiveTab}
        uploadedFiles={uploadedFiles}
      />

      <div className="main-content">
        {activeTab === 'chat' ? (
          <div className="chat-page">
            <div className="chat-container">
              <div className="chat-main">
                {isLoadingHistory ? (
                  <div className="loading-history">
                    <div className="spinner"></div>
                    <p>Chargement de l'historique...</p>
                  </div>
                ) : (
                  <ChatHistory conversations={conversations} />
                )}
              </div>
              
              <div className="chat-sidebar">
                <UploadedFilesList files={uploadedFiles} />
              </div>
            </div>
            
            <div className="chat-input-container">
              <ChatInterface 
                onAskQuestion={handleAskQuestion}
                isLoading={isLoading}
              />
            </div>
          </div>
        ) : (
          <FileUpload 
            onUpload={handleUploadFiles}
            onBack={() => setActiveTab('chat')}
          />
        )}
      </div>

      <footer className="footer">
        <p>Notebook LLM Assistant - Spring Boot + React RAG System</p>
      </footer>
    </div>
  );
}

export default App;
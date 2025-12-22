import API_CONFIG from '../config/api';

const { BASE_URL, ENDPOINTS } = API_CONFIG;

export const chatAPI = {
  // Récupérer l'historique des conversations
  async getConversations() {
    try {
      const response = await fetch(`${BASE_URL}${ENDPOINTS.CONVERSATIONS}`);
      if (!response.ok) throw new Error('Failed to fetch conversations');
      return await response.json();
    } catch (error) {
      console.error('Error fetching conversations:', error);
      throw error;
    }
  },

  // Envoyer une question au chatbot
  async askQuestion(question) {
    try {
      const response = await fetch(`${BASE_URL}${ENDPOINTS.ASK}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ question }),
      });
      
      if (!response.ok) throw new Error('Failed to get response');
      
      const data = await response.json();
      return data.response;
    } catch (error) {
      console.error('Error asking question:', error);
      throw error;
    }
  },

  // Uploader des fichiers PDF
  async uploadPDFs(files) {
    try {
      const formData = new FormData();
      files.forEach(file => {
        formData.append('pdfs', file);
      });

      const response = await fetch(`${BASE_URL}${ENDPOINTS.UPLOAD}`, {
        method: 'POST',
        body: formData,
      });
      
      if (!response.ok) throw new Error('Upload failed');
      
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error uploading files:', error);
      throw error;
    }
  }
};
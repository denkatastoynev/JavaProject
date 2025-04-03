import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

function UserPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const user = location.state?.user; // Retrieve user data passed via state

  const handleLogout = () => {
    navigate('/'); // Redirect back to the login page
  };

  return (
    <div
      style={{
        maxWidth: '600px',
        margin: '50px auto',
        padding: '20px',
        textAlign: 'center',
        fontFamily: 'Arial, sans-serif',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        borderRadius: '8px',
        backgroundColor: '#f9f9f9',
      }}
    >
      <h1 style={{ color: '#333', marginBottom: '20px' }}>
        Welcome, {user?.name || 'User'}!
      </h1>
      <p style={{ fontSize: '18px', color: '#555', marginBottom: '20px' }}>
        <strong>Email:</strong> {user?.email}
      </p>
      <p style={{ fontSize: '16px', color: '#777', marginBottom: '30px' }}>
        Authentication was successful. You are now logged in.
      </p>
      <button
        onClick={handleLogout}
        style={{
          padding: '10px 20px',
          cursor: 'pointer',
          backgroundColor: '#007BFF',
          color: 'white',
          border: 'none',
          borderRadius: '4px',
          fontSize: '16px',
          fontWeight: 'bold',
          transition: 'background-color 0.3s ease',
        }}
        onMouseOver={(e) => (e.target.style.backgroundColor = '#0056b3')}
        onMouseOut={(e) => (e.target.style.backgroundColor = '#007BFF')}
      >
        Logout
      </button>
    </div>
  );
}

export default UserPage;
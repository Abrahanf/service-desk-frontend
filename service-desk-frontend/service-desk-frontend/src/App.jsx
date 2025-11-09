import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import PasswordRecovery from './components/PasswordRecovery';
import TicketDetalle from './components/TicketDetalle';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/forgot-password" element={<PasswordRecovery />} />
        <Route path="/ticket/:id" element={<TicketDetalle />} />
      </Routes>
    </Router>
  );
}

export default App
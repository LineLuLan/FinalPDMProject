import React, { useEffect, useState } from 'react';
import axios from '@/lib/axios';

interface Props {
  fullName: string;
  dob: string;
  value: string;
  onChange: (val: string) => void;
}

const PssnSuggest: React.FC<Props> = ({ fullName, dob, value, onChange }) => {
  const [suggested, setSuggested] = useState('');
  const [checking, setChecking] = useState(false);
  const [isDuplicate, setIsDuplicate] = useState(false);

  useEffect(() => {
    // Suggest PSSN code when name and date of birth are provided
    if (fullName && dob) {
      const namePart = fullName.trim().split(' ').pop()?.slice(0, 3).toUpperCase() || 'XXX';
      const year = new Date(dob).getFullYear();
      setSuggested(`P${year}${namePart}`);
    }
  }, [fullName, dob]);

  useEffect(() => {
    // Check for duplicate PSSN code when user enters
    if (value) {
      setChecking(true);
      axios.get(`/api/patients/check-pssn?pssn=${value}`)
        .then(res => setIsDuplicate(res.data.exists))
        .catch(() => setIsDuplicate(false))
        .finally(() => setChecking(false));
    } else {
      setIsDuplicate(false);
    }
  }, [value]);

  return (
    <div>
      <label htmlFor="pssn" className="block text-sm font-medium text-gray-700 mb-1">
        Patient code (PSSN):
      </label>
      <div className="flex gap-2 items-center">
        <input
          type="text"
          id="pssn"
          name="pssn"
          value={value}
          onChange={e => onChange(e.target.value)}
          className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
          required
        />
        {suggested && (
          <button
            type="button"
            className="text-xs px-2 py-1 bg-gray-200 rounded hover:bg-gray-300"
            onClick={() => onChange(suggested)}
            tabIndex={-1}
          >
            Use suggestion: {suggested}
          </button>
        )}
      </div>
      {checking && <div className="text-xs text-gray-500 mt-1">Checking for duplicate code...</div>}
      {isDuplicate && <div className="text-xs text-red-500 mt-1">PSSN code already exists, please choose another!</div>}
    </div>
  );
};

export default PssnSuggest;

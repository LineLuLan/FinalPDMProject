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
    // Gợi ý mã PSSN khi có tên và ngày sinh
    if (fullName && dob) {
      const namePart = fullName.trim().split(' ').pop()?.slice(0, 3).toUpperCase() || 'XXX';
      const year = new Date(dob).getFullYear();
      setSuggested(`P${year}${namePart}`);
    }
  }, [fullName, dob]);

  useEffect(() => {
    // Kiểm tra trùng mã PSSN khi người dùng nhập
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
        Mã bệnh nhân (PSSN):
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
            Dùng gợi ý: {suggested}
          </button>
        )}
      </div>
      {checking && <div className="text-xs text-gray-500 mt-1">Đang kiểm tra trùng mã...</div>}
      {isDuplicate && <div className="text-xs text-red-500 mt-1">Mã PSSN đã tồn tại, hãy chọn mã khác!</div>}
    </div>
  );
};

export default PssnSuggest;

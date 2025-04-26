import { useEffect, useState } from 'react';

export interface BloodBank {
  bid: number;
  name: string;
}

export default function useBloodBanks() {
  const [bloodBanks, setBloodBanks] = useState<BloodBank[]>([]);
  useEffect(() => {
    fetch('/api/bloodBanks')
      .then(res => res.json())
      .then(data => setBloodBanks(data))
      .catch(() => setBloodBanks([]));
  }, []);
  return bloodBanks;
}

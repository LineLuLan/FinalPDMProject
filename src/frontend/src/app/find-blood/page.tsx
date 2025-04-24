"use client";

import { useState } from "react";

export default function FindBlood() {
  const [bloodType, setBloodType] = useState("");
  const [location, setLocation] = useState("");
  const [hasSearched, setHasSearched] = useState(false);

  const bloodTypes = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];
  const cities = ["New York", "Los Angeles", "Chicago", "Houston", "Phoenix"];

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setHasSearched(true);
  };

  return (
    <div className="py-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      {/* Header */}
      <div className="text-center mb-12">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          Find Blood Donors
        </h1>
        <p className="text-xl text-gray-600">
          Search for available blood donors in your area
        </p>
      </div>

      {/* Search Form */}
      <div className="max-w-2xl mx-auto mb-12">
        <form
          onSubmit={handleSearch}
          className="bg-white p-6 rounded-lg shadow-lg"
        >
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div>
              <label
                htmlFor="bloodType"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Blood Type
              </label>
              <select
                id="bloodType"
                value={bloodType}
                onChange={(e) => setBloodType(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                required
              >
                <option value="">Select Blood Type</option>
                {bloodTypes.map((type) => (
                  <option key={type} value={type}>
                    {type}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label
                htmlFor="location"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                City
              </label>
              <select
                id="location"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500"
                required
              >
                <option value="">Select City</option>
                {cities.map((city) => (
                  <option key={city} value={city}>
                    {city}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-red-600 text-white py-3 px-6 rounded-md hover:bg-red-700 transition-colors font-semibold"
          >
            Search Donors
          </button>
        </form>
      </div>

      {/* Results Section */}
      {hasSearched ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {/* Placeholder donor cards */}
          {[1, 2, 3, 4, 5].map((index) => (
            <div
              key={index}
              className="bg-white p-6 rounded-lg shadow-lg border border-gray-200"
            >
              <div className="flex items-center justify-between mb-4">
                <span className="text-2xl font-bold text-red-600">
                  {bloodType || "A+"}
                </span>
                <span className="bg-green-100 text-green-800 px-3 py-1 rounded-full text-sm">
                  Available
                </span>
              </div>
              <div className="mb-4">
                <h3 className="font-semibold text-gray-900">
                  Local Blood Bank
                </h3>
                <p className="text-gray-600">{location || "New York"}</p>
              </div>
              <div className="text-sm text-gray-500">
                <p>Last donation: 3 months ago</p>
                <p>Distance: 2.5 miles away</p>
              </div>
              <button className="mt-4 w-full bg-white text-red-600 border border-red-600 py-2 rounded-md hover:bg-red-50 transition-colors">
                Contact Donor
              </button>
            </div>
          ))}
        </div>
      ) : (
        <div className="text-center py-12 bg-gray-50 rounded-lg">
          <div className="text-6xl mb-4">🔍</div>
          <h2 className="text-xl font-semibold text-gray-900 mb-2">
            Enter criteria to begin your search
          </h2>
          <p className="text-gray-600">
            Select blood type and location to find available donors
          </p>
        </div>
      )}
    </div>
  );
}

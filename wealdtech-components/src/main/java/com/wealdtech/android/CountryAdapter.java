package com.wealdtech.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wealdtech.android.components.R;

import java.text.Collator;
import java.util.*;

/**
 * An adapter that contains a list of countries.
 */
public class CountryAdapter extends BaseAdapter
{
  private List<Country> countries;

  private Context context;

  public CountryAdapter(final Context context)
  {
    this.context = context;

    countries = new ArrayList<>();

    final String[] countryCodes = Locale.getISOCountries();
    for (final String countryCode : countryCodes)
    {
      final Locale countryLocale = new Locale("", countryCode);
      final String countryName = countryLocale.getDisplayCountry(Locale.getDefault());
      countries.add(new Country(countryName, countryCode));
    }

    final Collator collator = Collator.getInstance(Locale.getDefault());
    collator.setStrength(Collator.PRIMARY);
    Collections.sort(countries, new Comparator<Country>(){
      @Override
      public int compare(final Country left, final Country right)
      {
        return collator.compare(left.getName(), right.getName());
      }
    });
  }

  @Override
  public int getCount()
  {
    return countries.size();
  }

  @Override
  public Country getItem(final int position)
  {
    return countries.get(position);
  }

  @Override
  public long getItemId(final int position)
  {
    return position;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent)
  {
    final Country country = countries.get(position);

    final View view =
        convertView == null ? LayoutInflater.from(context).inflate(R.layout.country_adapter_item, parent, false) : convertView;

    final TextView name = (TextView)view.findViewById(R.id.country_adapter_item_name);
    name.setText(country.getName());

    return view;
  }

  /**
   * Provide the position for a given country code
   * @param code the ISO 2-letter country code
   * @return the position of the country in the list; will be {@code -1} if the code is unknown
   */
  public int positionForCode(final String code)
  {
    for (int i = 0; i < countries.size(); i++)
    {
      if (countries.get(i).getCode().equals(code))
      {
        return i;
      }
    }
    return -1;
  }

  public class Country
  {
    private final String name;
    private final String code;

    public Country(final String name, final String code)
    {
      this.name = name;
      this.code = code;
    }

    public String getName() { return name; }

    public String getCode() { return code; }
  }
}
